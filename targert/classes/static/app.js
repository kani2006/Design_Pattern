const api = path => `/api${path}`;
const json = (path, options = {}) => fetch(api(path), {headers: {'Content-Type': 'application/json'}, ...options}).then(async response => { if (!response.ok) throw new Error((await response.json()).message || 'Request failed'); return response.status === 204 ? null : response.json(); });
const $ = selector => document.querySelector(selector);

function renderTables(tables) {
    $('#table-total').textContent = `${tables.length} tables`;
    $('#open-count').textContent = tables.filter(table => table.status === 'AVAILABLE').length;
    $('#occupied-count').textContent = tables.filter(table => table.status === 'OCCUPIED').length;
    $('#table-list').innerHTML = tables.length ? tables.map(table => `<div class="table-card"><strong>Table ${table.tableNumber}</strong><small>${table.type.replaceAll('_', ' ')} · ${table.capacity} seats</small><span class="table-status ${table.status !== 'AVAILABLE' ? 'busy' : ''}">${table.status}</span></div>`).join('') : '<div class="empty">Add a table to begin.</div>';
    const available = tables.filter(table => table.status === 'AVAILABLE');
    $('#allocation-table').innerHTML = available.length ? available.map(table => `<option value="${table.id}">Table ${table.tableNumber} · ${table.capacity} seats</option>`).join('') : '<option>No open tables</option>';
    $('#allocate-button').disabled = !available.length;
}

function renderQueue(entries) {
    const waiting = entries.filter(entry => entry.status === 'WAITING');
    $('#waiting-count').textContent = waiting.length;
    $('#queue-list').innerHTML = waiting.length ? waiting.map((entry, index) => `<div class="queue-item"><span class="queue-number">${String(index + 1).padStart(2, '0')}</span><div><strong>${entry.customer.name}</strong><small>${entry.customer.partySize} guests · ${new Date(entry.joinedAt).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})}</small></div>${entry.customer.vip ? '<span class="vip">VIP</span>' : ''}</div>`).join('') : '<div class="empty">No guests waiting yet.</div>';
}

async function refresh() { try { const [tables, entries] = await Promise.all([json('/tables'), json('/queue')]); renderTables(tables); renderQueue(entries); } catch (error) { $('#notice').textContent = error.message; } }
$('#guest-form').addEventListener('submit', async event => { event.preventDefault(); const data = Object.fromEntries(new FormData(event.target)); try { const customer = await json('/customers', {method: 'POST', body: JSON.stringify({...data, partySize: Number(data.partySize), vip: data.vip === 'on'})}); await json('/queue', {method: 'POST', body: JSON.stringify({customerId: customer.id})}); event.target.reset(); $('#notice').textContent = `${customer.name} added to the waiting room.`; await refresh(); } catch (error) { $('#notice').textContent = error.message; } });
$('#table-form').addEventListener('submit', async event => { event.preventDefault(); const data = Object.fromEntries(new FormData(event.target)); try { await json('/tables', {method: 'POST', body: JSON.stringify({...data, tableNumber: Number(data.tableNumber)})}); event.target.reset(); $('#notice').textContent = 'Table added to the floor plan.'; await refresh(); } catch (error) { $('#notice').textContent = error.message; } });
$('#allocate-button').addEventListener('click', async () => { try { await json('/queue/allocate', {method: 'POST', body: JSON.stringify({tableId: Number($('#allocation-table').value), strategy: $('#strategy').value})}); $('#notice').textContent = 'Guest allocated. Notification observers dispatched.'; await refresh(); } catch (error) { $('#notice').textContent = error.message; } });
$('#reserve-button').addEventListener('click', async () => { try { const time = $('#reservation-time').value; await json('/reservations', {method: 'POST', body: JSON.stringify({customerId: Number($('#reservation-customer').value), tableId: Number($('#allocation-table').value), reservationTime: new Date(time).toISOString()})}); $('#notice').textContent = 'Reservation confirmed. This table is now owned by the guest.'; await refresh(); } catch (error) { $('#notice').textContent = error.message; } });
function updateClock() { $('#clock').textContent = new Date().toLocaleString([], {weekday: 'long', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'}); }
updateClock(); setInterval(updateClock, 30000); refresh();