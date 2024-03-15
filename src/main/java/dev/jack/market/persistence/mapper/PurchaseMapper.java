package dev.jack.market.persistence.mapper;

import dev.jack.market.domain.Purchase;
import dev.jack.market.persistence.entity.Compra;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

//creo que hay que ponerlo uses={} solo cuando usara otro mapper
@Mapper(componentModel = "spring", uses = {PurchaseItemMapper.class})
public interface PurchaseMapper {

    @Mappings({
            @Mapping(source = "idCompra", target = "purchaseId"),
            @Mapping(source = "idCliente", target = "clientId"),
            @Mapping(source = "fecha", target = "dateTime"),
            @Mapping(source = "medioPago", target = "paymentMethod"),
            @Mapping(source = "comentario", target = "comment"),
            @Mapping(source = "comprasProductos", target = "items"),
            @Mapping(source = "estado", target = "state")
    })
    Purchase toPurchase(Compra compra);

    //automaticamente tomara lo de arriba
    List<Purchase> toPurchases(List<Compra> compras);

    @InheritInverseConfiguration
    //puede ser solo @Mapping
    @Mappings({
            @Mapping(target = "cliente", ignore = true)
    })
    Compra toCompra(Purchase purchase);

}
