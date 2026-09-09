package com.restaurant.queue.pattern.state;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;
import com.restaurant.queue.model.TableType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableStateContextTest {
    @Test
    void occupiedTableMustBeCleanedBeforeItBecomesAvailable() {
        RestaurantTable table = new RestaurantTable(1L, 1, 4, TableType.FOUR_SEATER);
        TableStateContext context = new TableStateContext();
        context.transition(table, TableStatus.OCCUPIED);

        assertThrows(IllegalStateException.class, () -> context.transition(table, TableStatus.AVAILABLE));
        context.transition(table, TableStatus.CLEANING);
        context.transition(table, TableStatus.AVAILABLE);
        assertEquals(TableStatus.AVAILABLE, table.getStatus());
    }
}