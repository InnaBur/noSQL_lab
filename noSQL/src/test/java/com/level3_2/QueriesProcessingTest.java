package com.level3_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class QueriesProcessingTest {
        QueriesProcessing queriesProcessing = new QueriesProcessing();
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ;
        ResultSet resultSet = mock(ResultSet.class);

        @BeforeEach
        void setUp() throws SQLException {

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
        }

        @Test
        void getShopId() throws SQLException {
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("id")).thenReturn(200);

            List<Integer> shopIdList = queriesProcessing.getShopId(connection);

            assertEquals(1, shopIdList.size());
            assertEquals(200, shopIdList.get(0));

            verify(connection, times(1)).createStatement();
            verify(resultSet, times(2)).next();
            verify(resultSet, times(1)).close();
        }

        @Test
        void getTypeIdTest() throws SQLException {

            when(resultSet.next()).thenReturn(true, true, true, false);
            when(resultSet.getInt("id")).thenReturn(10, 11, 12);

            List<Integer> typeIdList = queriesProcessing.getTypeId(connection);

            assertEquals(3, typeIdList.size());
            assertEquals(12, typeIdList.get(2));

            verify(connection, times(1)).createStatement();
            verify(resultSet, times(4)).next();
            verify(statement, times(1)).close();
        }

        @Test
        void executeQueryTest() throws IOException, SQLException {

            queriesProcessing.executeQuery(connection, "test2.sql", "log message");

            verify(statement, times(1)).execute(anyString());
            verify(connection, times(1)).commit();
        }

    }
