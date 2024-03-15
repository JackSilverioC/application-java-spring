package dev.jack.market.persistence;

import dev.jack.market.domain.Product;
import dev.jack.market.domain.repository.ProductRepository;
import dev.jack.market.persistence.crud.ProductoCrudRepository;
import dev.jack.market.persistence.entity.Producto;
import dev.jack.market.persistence.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository implements ProductRepository {
    //si no se inicializa dara nullpointerexception pq seria null.(...)
    //si se usa en variables debemos de asegurarnos que el objeto es un componente
    //de spring

    private ProductoCrudRepository productoCrudRepository;

    private ProductMapper productMapper;

    @Autowired
    public ProductoRepository(ProductoCrudRepository productoCrudRepository, ProductMapper productMapper) {
        this.productoCrudRepository = productoCrudRepository;
        this.productMapper = productMapper;
    }

    //poner override
    @Override
    public List<Product> getAll(){

        List<Producto> productos = (List<Producto>) productoCrudRepository.findAll();
        //si no se inicia mapper es como si fuera "null.toproducts()"
        return productMapper.toProducts(productos);

    }

    @Override
    public Optional<List<Product>> getByCategory(int categoryId) {

        List<Producto> productos = productoCrudRepository
                .findByIdCategoriaOrderByNombreAsc(categoryId);

        return Optional.ofNullable(productMapper.toProducts(productos));

    }

    @Override
    public Optional<List<Product>> getScarseProducts(int quantity) {

        Optional<List<Producto>> productos = productoCrudRepository
                .findByCantidadStockLessThanAndEstado(quantity, true);

        //Si este Optional contiene una lista de productos (productos),
        //se utiliza el ProductMapper para convertir esta lista de productos
        //de entidad (Producto) en una lista de productos de dominio (Product).
        //el .map no cambia el original que es Optional

        return  productos.map(prodsList -> productMapper.toProducts(prodsList));
    }

    @Override
    public Optional<Product> getProduct(int productId) {
        return productoCrudRepository.findById(productId).map(prod->
                productMapper.toProduct(prod));
    }

    @Override
    public Product save(Product product) {

        Producto producto = productMapper.toProducto(product);
        return productMapper.toProduct(productoCrudRepository
                .save(producto));
    }

    @Override
    public void delete(int productId){
        productoCrudRepository.deleteById(productId);
    }

    /*
    public List<Producto> getByCategoria(int idCategoria){
        return productoCrudRepository.findByIdCategoriaOrderByNombreAsc(idCategoria);
    }
    */


    /*
    public Optional<List<Producto>> getEscasos(int cantidad){
        return productoCrudRepository.findByCantidadStockLessThanAndEstado(cantidad,true);
    }
    */

    /*

    //Aqui no es necesario declararlo en el crudRepository
    //porque la misma clase nos ofrece findById
    public Optional<Producto> getProducto(int idProducto){
        return productoCrudRepository.findById(idProducto);
    }
     */

    /*
    public Producto save(Producto producto){
        return productoCrudRepository.save(producto);
    }
    */

}
