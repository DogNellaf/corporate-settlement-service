package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practice5.main.models.TppRefProductClass;

import java.util.List;

public interface TppRefProductClassRepository extends JpaRepository<TppRefProductClass, Long> {
//    List<TppRefProductClass> findByValueAndAccountType(String value, String accountType);
//    @Query(value = "SELECT * " +
//                   "from item i " +
//                   "where (lower(i.name) like %:text% or lower(i.description) like %:text%) AND i.available = True",
//            nativeQuery = true)
//    List<Item> findByValue(String text);
}
