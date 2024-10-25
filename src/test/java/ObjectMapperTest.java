import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * @author NanNan Wang
 */
public class ObjectMapperTest {


    class User {
        private String name;
        private LocalDate birthday;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }
    }

    @Test
    public void testDate() throws JsonProcessingException {
        User user = new User();
        user.setName("nan");
        user.setBirthday(LocalDate.now());
        ObjectMapper objectMapper = new ObjectMapper();
        // 添加 JavaTimeModule 支持 Java 8 时间类
        objectMapper.registerModule(new JavaTimeModule());
        // 需要特定的日期格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        System.out.println(objectMapper.writeValueAsString(user));;

    }
}
