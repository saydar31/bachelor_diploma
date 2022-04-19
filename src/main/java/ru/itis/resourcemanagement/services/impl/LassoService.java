package ru.itis.resourcemanagement.services.impl;

import org.springframework.stereotype.Component;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.regression.LASSO;
import smile.regression.LinearModel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LassoService {

    private final DataSource dataSource;

    public LassoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void test() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select val, val * 2 + 1 as y from test")
        ) {
            ResultSet resultSet = statement.executeQuery();
            DataFrame dataFrame = DataFrame.of(resultSet);
            LinearModel lasso = LASSO.fit(Formula.lhs("y"), dataFrame);
            System.out.println(lasso);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Formula factTime = Formula.lhs("factTime");
        //LASSO.fit()
    }
}
