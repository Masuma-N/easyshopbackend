# EasyShopBackEnd Application

#### 
The EasyShopBackEnd Application is a backend Spring Boot Java API project that supports the EasyShop e-commerce website.
The backend portion of this project needed to be altered to:

- register a new user
- add a new user
- login as admin
- add a category as admin
- get newly added categories by ID
- delete categories as admin
- and search products

### Easy Shop Website!
![Screenshot 2023-07-05 at 11.02.01 PM.png](..%2F..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202023-07-05%20at%2011.02.01%20PM.png)
### Searching Products by Filter
![Screenshot 2023-07-05 at 11.05.00 PM.png](..%2F..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202023-07-05%20at%2011.05.00%20PM.png)
### Searching Products by Color
![Screenshot 2023-07-05 at 11.05.53 PM.png](..%2F..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202023-07-05%20at%2011.05.53%20PM.png)
### Searching Products by Price Range
![Screenshot 2023-07-05 at 11.06.31 PM.png](..%2F..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202023-07-05%20at%2011.06.31%20PM.png)

The project was divided into 2 phases:

## Phase 1:
In phase 1 the class MySqlCategoryDao needed its methods implemented.
The methods are:
- getAllCategories()-  Retrieves all categories from the database.
- getById()- Retrieves a specific category from the database based on its ID.
- create()- Creates a new category in the database.
- update()- Updates an existing category in the database based on its ID.
- delete() - Deletes a category from the database based on its ID.

In the class CategoriesController methods needed to be implemented.
The methods are:
- getAll()- GET request to retrieve all categories.
- getById()- GET request to retrieve a category by ID.
- addCategory()- POST request to add a new category (accessible only to administrators).
- updateCategory()- PUT request to update a category by ID (accessible only to administrators).
- deleteCategory()- DELETE request to delete a category by ID (accessible only to administrators).


## Phase 2:
## Bug Fixes

Search/Filter Bug:

- The search function was not returning the expected results.
- The query inside the  Search method of MYSqlProduct  was updated to fix the issue.
- The SQL query inside the method was missing a clause for the maximum price:



> **"SELECT * FROM products " +
  "WHERE (category_id = ? OR ? = -1) " +
  "   AND (price <= ? OR ? = -1) " +
  "   AND (price >= ? OR ? = -1) " // added to look for products between min and max price +   
  "   AND (color = ? OR ? = '') ";**

The second thing that needed to be added in the search method in order for the search functionality to work properly was:
> **statement.setBigDecimal(3, maxPrice);**
> **statement.setBigDecimal(4, maxPrice);**

This was needed in order for this SQL clause to have the proper parameter:

    "   AND (price <= ? OR ? = -1) "

Duplicate Products Bug:

- The bug caused a new product to be created whenever the admin tried to update a product.
- The UpdateProduct() method in the ProductController class was altered to fix this issue.
- The method now correctly uses the update() method of the productDao to update the product with the provided ID.

This code needed to be changed 
From:
> **productDao.create(product); // creates a new product**

To:
> **productDao.update(id, product); // updates an existing product**

### One interesting piece of code
                statement.executeUpdate();
- The update() method is used to modify the existing row with the specified value
- The statement.executeUpdate() method is used to execute an SQL statement that modifies the database.
- It executes the SQL statement represented by the PreparedStatement object.
- It performs the database operation specified by the SQL statement, which can be an INSERT, UPDATE, or DELETE operation.



