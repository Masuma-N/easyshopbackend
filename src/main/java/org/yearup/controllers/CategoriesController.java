package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// annotations for cross site origin requests
//handles HTTP requests related to categories.***
@RestController// annotation to make this rest controller
//controller receives incoming HTTP requests and amps them to appropiate methods
@RequestMapping("/categories")// makes the controller the endpoint
// for the following url http://localhost:8080/categories

@CrossOrigin// allows requests from different origins
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }
    // add the appropriate annotation for a get action
    @GetMapping()//maps HTTP GET requests to retrieve all categories
    @PreAuthorize("permitAll()")//allows any user to access the endpoints.
    public List<Category> getAll()
    {
        // find and return all categories
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")//HTTP GET requests with a path variable (id)
    @PreAuthorize("permitAll()")// any user to access endpoint

    public ResponseEntity<Category> getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products

    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")//
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping//maps HTTP POST requests to add a new category.
    @ResponseStatus(HttpStatus.CREATED)//annotation sets the HTTP response status to
    // 201 Created for successful addCategory() requests.
    @PreAuthorize("hasRole('ROLE_ADMIN')")//  annotation so that only an ADMIN can access endpoint
    //R.E used to encapsulate the HTTP response that is sent back to the client after adding a category**
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        // inserts the category
        try {
            return categoryDao.create(category);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping//maps HTTP PUT requests to update an existing category
    @PreAuthorize("hasRole('ROLE_ADMIN')")//ensure that only an ADMIN can access endpoint
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {

        // update the category by id
        category.setCategoryId(id);
        categoryDao.update(id, category);
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("/{id}")// maps HTTP DELETE requests to the deleteCategory() method
    @ResponseStatus(HttpStatus.NO_CONTENT)// When this method is invoked and successfully deletes a category,
    // the HTTP response will have a status code of 204 No Content
    @PreAuthorize("hasRole('ROLE_ADMIN')")//  only an ADMIN can call this function
    public void deleteCategory(@PathVariable("id")  int id)
    {
        try {
            Category existingCategory = categoryDao.getById(id);
            if (existingCategory == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            categoryDao.delete(id);//calls method to delete the category from the database.
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);//Status code 500 is thrown
        }
    }
}
