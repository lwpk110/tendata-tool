package cn.tendata.batch.northamerica.item;

public interface ShipmentResolver<T> {

    Shipment resolve(T item);
}
