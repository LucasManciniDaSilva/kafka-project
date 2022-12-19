package br.com.lucas.ecommerce;


import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {


    public static void main(String[] args) throws SQLException {
        var myService = new CreateUserService();
        try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                myService::parse,
                Order.class,
                Map.of())) {
            service.run();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final Connection connection;
    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:myDB.sqlite";
        this.connection = DriverManager.getConnection(url);
        try {
            this.connection.createStatement().execute("create table Users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200))");
        } catch(SQLException ex) {
            // not the best solution for database schema evolution.
            // but this is not our goal here.
            ex.printStackTrace();
        }
    }

    private boolean isNew(String email) throws SQLException {
        var statement = connection.prepareStatement("select uuid from Users " +
                "where email = ? limit 1");
        statement.setString(1, email);
        var results = statement.executeQuery();
        return !results.next();
    }

    private void insertNew(String email) throws SQLException {
        var statement = connection.prepareStatement("insert into Users (uuid, email) " +
                "values (?,?)");
        statement.setString(1, UUID.randomUUID().toString());
        statement.setString(2, email);
        statement.execute();
        System.out.println("User " + email + " inserted.");
    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for new user");
        var order = record.value();
        if (isNew(order.getEmail())) {
           insertNew(order.getEmail());
        }
    }

}