package com.example.scleradb.jdbc;

import java.util.Properties;
import java.sql.*;
import java.util.ArrayList;

public class JdbcExample {
    static String jdbcUrl = "jdbc:scleradb";

    public static void main(String[] args) {
        if( args.length > 0 ) {
            try {
                if( args[0] == "--init" ) {
                    initialize(); // initialize Sclera
                } else runQueries(args);  // execute queries
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // initialize Sclera
    private static void initialize() throws SQLException {
        // we are initializing the schema, no need to check and validate
        Properties props = new Properties();
        props.setProperty("checkSchema", "false");

        // get a JDBC connection to Sclera
        Connection conn = DriverManager.getConnection(jdbcUrl, props);

        try {
            // display warning, if any
            SQLWarning warning = conn.getWarnings();
            if( warning != null ) System.out.println(warning.getMessage());

            // initialize the schema by executing the statement "create schema"
            Statement stmt = conn.createStatement();

            try {
                stmt.executeUpdate("create schema");
            } finally {
                stmt.close();
            }
        } finally {
            conn.close();
        }
    }

    // run queries on Sclera
    private static void runQueries(String[] queries) throws SQLException {
        // get a JDBC connection to Sclera
        Connection conn = DriverManager.getConnection(jdbcUrl);

        try {
            // display warning, if any
            SQLWarning warning = conn.getWarnings();
            if( warning != null ) System.out.println(warning.getMessage());

            // create a statement to execute queries
            Statement stmt = conn.createStatement();

            // iterate over the input queries
            try {
                for( String query: queries ) {
                    // execute query
                    ResultSet rs = stmt.executeQuery(query);

                    try {
                        // result metadata
                        ResultSetMetaData metaData = rs.getMetaData();
                        int n = metaData.getColumnCount();
                        ArrayList<String> colNames = new ArrayList<String>();
                        for( int i = 1; i <= n; i++ ) {
                            colNames.add(metaData.getColumnLabel(i));
                        }

                        // display column names
                        System.out.println(String.join(", ", colNames));

                        // display each row in the result
                        while( rs.next() ) {
                            ArrayList<String> rowVals = new ArrayList<String>();
                            for( int i = 1; i <= n; i++ ) {
                                rowVals.add(rs.getString(i));
                            }

                            System.out.println(String.join(", ", rowVals));
                        }
                    } finally {
                        rs.close();
                    }
                }
            } finally {
                stmt.close();
            }
        } finally {
            conn.close();
        }
    }
}
