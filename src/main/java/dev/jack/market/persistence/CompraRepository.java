package dev.jack.market.persistence;

import dev.jack.market.domain.Purchase;
import dev.jack.market.domain.repository.PurchaseRepository;
import dev.jack.market.persistence.crud.CompraCrudRepository;
import dev.jack.market.persistence.entity.Compra;
import dev.jack.market.persistence.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompraRepository implements PurchaseRepository {

    private CompraCrudRepository compraCrudRepository;

    private PurchaseMapper mapper;

    @Autowired
    public CompraRepository(CompraCrudRepository compraCrudRepository, PurchaseMapper mapper) {
        this.compraCrudRepository = compraCrudRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Purchase> getAll() {
        return mapper.toPurchases((List<Compra>) compraCrudRepository.findAll());
    }

    @Override
    public Optional<List<Purchase>> getByClient(String clientId) {
        return compraCrudRepository.findByIdCliente(clientId).map(compras->
                mapper.toPurchases(compras));
    }

    @Override
    public Purchase save(Purchase purchase) {

        Compra compra = mapper.toCompra(purchase);

        //estoy haciendo esto pq se necesita acceder a los productos desde el
        //lado de la compra despues de guardarlos (devuelve Purchase asi que
        // necesito la relacion desde compras)
        compra.getComprasProductos().forEach(comprasProducto ->
                comprasProducto.setCompra(compra));

        return mapper.toPurchase(compraCrudRepository.save(compra));
    }
}
