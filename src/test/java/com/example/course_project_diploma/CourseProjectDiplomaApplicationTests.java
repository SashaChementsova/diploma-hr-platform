package com.example.course_project_diploma;

import com.model.UserDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseProjectDiplomaApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testAddObjectToDatabase() {
        UserDetail myObject = new UserDetail();
        myObject.setName("Sasha");
        myObject.setSurname("Chementsova");
        myObject.setPhone("+375442569998");
        myObject.setPassword("sasha222");
        myObject.setBirthday(new java.sql.Date(new java.util.Date().getTime()));
        myObject.setEmail("chementsov2102@gmail.com");
        // Сохраняем объект в базе данных
        entityManager.persist(myObject);

        // Извлекаем объект из базы данных
        UserDetail result = entityManager.find(UserDetail.class, myObject.getIdUserDetails());

        // Проверяем, что объект был успешно добавлен
        assertNotNull(result);
        assertEquals(myObject.getName(), result.getName());
    }

    @Test
    public void testCompareDates() {
        // Создаем новый объект
        String date1 = "2011-11-21";
        String date2 = "2012-11-21";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne = null;
        Date dateTwo = null;
        try {
            dateOne = format.parse(date1);
            dateTwo = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Количество дней между датами в миллисекундах
        long difference = dateTwo.getTime() - dateOne.getTime();
        // Перевод количества дней между датами из миллисекунд в дни
        double days = difference / (24 * 60 * 60 * 1000); // миллисекунды / (24ч * 60мин * 60сек * 1000мс)
        double years=days/365;
        int differenceResult= (int) Math.floor(years);
        Assertions.assertEquals(differenceResult,1);
    }
}