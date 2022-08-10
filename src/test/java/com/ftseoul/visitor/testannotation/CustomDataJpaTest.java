package com.ftseoul.visitor.testannotation;


import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.p6spyconfig.P6spyLogMessageFormatConfiguration;
import com.ftseoul.visitor.service.StaffService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)

@DataJpaTest(showSql = false, bootstrapMode = BootstrapMode.LAZY)
@Import(P6spyLogMessageFormatConfiguration.class)
@ContextConfiguration(classes = {Seed.class, StaffService.class})
public @interface CustomDataJpaTest {
}
