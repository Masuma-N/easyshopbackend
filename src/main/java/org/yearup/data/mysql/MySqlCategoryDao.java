package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    //receives a DataSource object and calls the constructor
    // of the parent class MySqlDaoBase to initialize the data source.
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        //SQL query to select all rows from the categories table
        // maps each row to a Category object using the mapRow() method, and returns a list of categories.
        String query = "SELECT * FROM categories";
     //   try- resources block to automatically manage the resources (Connection, PreparedStatement, ResultSet)
        //   used for executing the SQL query.
        try (Connection connection = dataSource.getConnection();
             //The dataSource is used to obtain a connection to the database
             PreparedStatement statement = connection.prepareStatement(query);
             //executeQuery() method is called to execute the query and obtain a ResultSet
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        //query string to select all columns from the categories table where the category_id
        // matches the given categoryId.
        String query = "SELECT * FROM categories WHERE category_id = ?";
        //automatically manage the resources Connection and PreparedStatement
        // used for executing the SQL query
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //sets the value of the parameter placeholder (?) in the SQL
            // query to the given categoryId
            statement.setInt(1, categoryId);
            //executeQuery() method is called on the PreparedStatement to
            // execute the SQL query and obtain a ResultSet containing the result.
            try (ResultSet resultSet = statement.executeQuery()) {
                //checks if there is at least one row in the ResultSet using the next() method.
                // If there is a row, it calls the mapRow() method to map the row
                // to a Category object and returns the result.
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }//handling any SQLException that might occur during the execution of the SQL query
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<Category> create(Category category) {
        //SQL query string to insert a new row into the categories table.
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        //try-with-resources block to automatically manage the resources
        // (Connection and PreparedStatement) used for executing the SQL query
        try (Connection connection = dataSource.getConnection();
             //dataSource is used to obtain a connection to the database
             //PreparedStatement is then created with the SQL query
             // PreparedStatement.RETURN_GENERATED_KEYS flag is set to indicate that generated keys should be returned.
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
           // bind the values of the name and description properties of the category object
            // to the corresponding parameter placeholders (?)
                    statement.setString(2, category.getDescription());
                    //executes the SQL INSERT statement, which inserts a new row into the categories table
            // //with the provided values for name and description.
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int generatedId = resultSet.getInt(1);
                    category.setCategoryId(generatedId);
                    return new ResponseEntity<>(category, HttpStatus.CREATED);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void update(int categoryId, Category category) {
        //SQL query string to update the categories table
        String query = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
       // bind the new values of the name and description properties of the category object
            // to the corresponding parameter placeholders (?) in the prepared statement.
            // This sets the values that will be updated in the name and description columns.
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

//update() method is used to modify the existing row with the specified categoryId
// by updating the name and description columns of that row
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId) {
        //defines an SQL query string to delete a row from the categories table
        String query = "DELETE FROM categories WHERE category_id = ?";
        //manage the resources (Connection and PreparedStatement) used for executing the SQL query
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //sets the value of the parameter placeholder (?) in the prepared statement to the provided categoryId.
            // This ensures that the query is executed for the specific categoryId value.
            statement.setInt(1, categoryId);

            // executes the SQL DELETE statement, which removes the row with the specified category_id
            // from the categories table
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


   //helper method used to map a ResultSet row to a Category object
    private Category mapRow(ResultSet row) throws SQLException {
        //retrieves the value of the "category_id" column from the ResultSet row
        // and assigns it to the categoryId variable.
        int categoryId = row.getInt("category_id");
        //retrieves name and assign it to name variable
        String name = row.getString("name");
        String description = row.getString("description");


       //creates a new Category object using an inner class
        return new Category() {

            //sets the categoryId, name, and description properties of the Category object
            // using the values retrieved from the ResultSet row.
            {//created Category object is returned by the mapRow() method
                setCategoryId(categoryId);
                setName(name);
                setDescription(description);

                //used to take a row from a ResultSet and extract the values of its columns.
                // creates a new Category object and sets the extracted values
                // to the corresponding properties of the object.
            }

        };
    }
}




