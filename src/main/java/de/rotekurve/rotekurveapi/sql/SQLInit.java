package de.rotekurve.rotekurveapi.sql;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLInit implements ResourceLoaderAware {
    private final DataSource dataSource;

    private ResourceLoader resourceLoader;

    @Autowired
    public SQLInit(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        executeSqlStatements();
    }

    private void executeSqlStatements() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            Resource resource = resourceLoader.getResource("classpath:schema.sql");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                List<String> sqlStatements = new ArrayList<>();
                StringBuilder currentStatement = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                        currentStatement.append(line).append("\n");

                        if (line.contains(";")) {
                            sqlStatements.add(currentStatement.toString());
                            currentStatement = new StringBuilder();
                        }
                    }
                }

                for (String sql : sqlStatements) {
                    statement.executeUpdate(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
