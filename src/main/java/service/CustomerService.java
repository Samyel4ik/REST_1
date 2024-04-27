package service;

import dto.CustomerDTO;

/**
 * Сервис для работы с покупателямию.
 * Этот интерфейс предоставляет методы для получения, добавления, удаления, обновления автора.
 */
public interface CustomerService {

    /**
     * Метод получения покупателя по идентификатору.
     *
     * @param id идентификатор ппокупателя.
     * @return объект типа CustomerDTO, содержащий информацию о покупателе.
     */
    CustomerDTO getCustomer(int id);

    /**
     * Метод удаления покупателя.
     *
     * @param customerDTO информация о покупателе.
     */
    void deleteCustomer(CustomerDTO customerDTO);

    /**
     * Метод добавления покупателя.
     *
     * @param customerDTO информация о покупателе.
     */
    void addCustomer(CustomerDTO customerDTO);

    /**
     * Метод обновления покупателя.
     *
     * @param customerDTO информация о покупателе.
     */
    void updateCustomerName(CustomerDTO customerDTO);
}
