package ru.itis.resourcemanagement.services.impl;

import org.springframework.stereotype.Component;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.regression.LinearModel;
import smile.regression.OLS;
import smile.regression.RandomForest;
import smile.validation.metric.Accuracy;
import smile.validation.metric.MSE;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class LassoService {

    private final DataSource dataSource;

    public LassoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @PostConstruct
    public void test(){
        recalculateCoefficients(1L);
    }

    public void recalculateCoefficients(Long taskTypeId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("select square, fact as fact_time from generated")
        ) {
            //statement.setLong(1, taskTypeId);
            ResultSet resultSet = statement.executeQuery();
            DataFrame dataFrame = DataFrame.of(resultSet);

            Map<Boolean, List<Tuple>> trainTest = dataFrame.stream()
                    .collect(Collectors.groupingBy(t -> Math.random() > .8, Collectors.toList()));

            DataFrame train = DataFrame.of(trainTest.get(false));
            DataFrame test = DataFrame.of(trainTest.get(true));


            RandomForest randomForest = RandomForest.fit(Formula.lhs("fact_time"), train, 10, 0, 4, 16, 5, 1.0);
            System.out.println(randomForest);
            LinearModel linearModel = OLS.fit(Formula.lhs("fact_time"), train, "svd", true, false);
            System.out.println(linearModel);

            double[] truth = test.column("fact_time").toDoubleArray();

            double[] predictRandomForest = randomForest.predict(test.drop("fact_time"));
            double[] predictOms = linearModel.predict(test.drop("fact_time"));
            System.out.printf("MSE random forest = %s %n", MSE.of(truth, predictRandomForest));
            System.out.printf("MSE linear = %s %n", MSE.of(truth, predictOms));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
