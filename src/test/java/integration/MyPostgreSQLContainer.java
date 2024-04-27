package integration;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

public class MyPostgreSQLContainer extends PostgreSQLContainer {
    private static final String INIT_SCRIPT_PATH = "/docker-entrypoint-initdb.d/init.sql";

    public MyPostgreSQLContainer() {
        super();
        // Добавляем скрипт инициализации в контейнер
        this.withCopyFileToContainer(MountableFile.forClasspathResource("init.sql"), INIT_SCRIPT_PATH);
        // Устанавливаем имя и пароль для доступа к базе данных
        this.withUsername("test");
        this.withPassword("test");
    }
}
