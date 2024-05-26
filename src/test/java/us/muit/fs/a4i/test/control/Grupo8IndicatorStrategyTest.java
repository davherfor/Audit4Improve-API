package us.muit.fs.a4i.test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import us.muit.fs.a4i.control.Grupo8IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;

class Grupo8IndicatorTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    public void testCalcIndicator() throws NotAvailableMetricException {
        // Creamos los mocks
        ReportItemI<Double> mockIssuesCerrados = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockIssuesTotales = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockPullRequestsCerrados = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockPullRequestsTotales = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockContribuyentesActivos = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockContribuyentesTotales = Mockito.mock(ReportItemI.class);

        // Configuramos los mocks
        Mockito.when(mockIssuesCerrados.getName()).thenReturn("issuesCerrados");
        Mockito.when(mockIssuesCerrados.getValue()).thenReturn(40.0);
        Mockito.when(mockIssuesTotales.getName()).thenReturn("issuesTotales");
        Mockito.when(mockIssuesTotales.getValue()).thenReturn(50.0);

        Mockito.when(mockPullRequestsCerrados.getName()).thenReturn("pullRequestsCerrados");
        Mockito.when(mockPullRequestsCerrados.getValue()).thenReturn(30.0);
        Mockito.when(mockPullRequestsTotales.getName()).thenReturn("pullRequestsTotales");
        Mockito.when(mockPullRequestsTotales.getValue()).thenReturn(50.0);

        Mockito.when(mockContribuyentesActivos.getName()).thenReturn("contribuyentesActivos");
        Mockito.when(mockContribuyentesActivos.getValue()).thenReturn(10.0);
        Mockito.when(mockContribuyentesTotales.getName()).thenReturn("contribuyentesTotales");
        Mockito.when(mockContribuyentesTotales.getValue()).thenReturn(20.0);

        // Creamos la calculadora del indicador
        Grupo8IndicatorStrategy indicadorCalc = new Grupo8IndicatorStrategy();

        // Calculamos el indicador
        List<ReportItemI<Double>> metricas = Arrays.asList(mockIssuesCerrados, mockIssuesTotales,
                mockPullRequestsCerrados, mockPullRequestsTotales, mockContribuyentesActivos, mockContribuyentesTotales);
        ReportItemI<Double> resultado = indicadorCalc.calcIndicator(metricas);

        // Comprobamos que el resultado es el esperado
        Assertions.assertEquals("comprehensiveIndicator", resultado.getName());
        Assertions.assertEquals(58.0, resultado.getValue(), 0.01); // El valor esperado es 58.0
        Assertions.assertEquals(IndicatorState.CRITICAL, resultado.getIndicator());
        Assertions.assertDoesNotThrow(() -> indicadorCalc.calcIndicator(metricas));
    }

    @Test
    public void testCalcIndicatorThrowsNotAvailableMetricException() {
        // Creamos un mock
        ReportItemI<Double> mockIssuesCerrados = Mockito.mock(ReportItemI.class);

        // Configuramos el mock
        Mockito.when(mockIssuesCerrados.getName()).thenReturn("issuesCerrados");
        Mockito.when(mockIssuesCerrados.getValue()).thenReturn(40.0);

        // Creamos la calculadora del indicador
        Grupo8IndicatorStrategy indicadorCalc = new Grupo8IndicatorStrategy();

        // Probamos con una sola métrica
        List<ReportItemI<Double>> metricas = Arrays.asList(mockIssuesCerrados);
        // Comprobamos que se lanza la excepción
        NotAvailableMetricException exception = Assertions.assertThrows(NotAvailableMetricException.class,
                () -> indicadorCalc.calcIndicator(metricas));
    }

    @Test
    public void testRequiredMetrics() {
        // Creamos la calculadora del indicador
        Grupo8IndicatorStrategy indicadorCalc = new Grupo8IndicatorStrategy();

        // Obtenemos las métricas del indicador
        List<String> metricas = indicadorCalc.requiredMetrics();

        // Comprobamos que el resultado es el esperado
        List<String> metricasEsperadas = Arrays.asList(
                "issuesCerrados", "issuesTotales",
                "pullRequestsCerrados", "pullRequestsTotales",
                "contribuyentesActivos", "contribuyentesTotales");
        Assertions.assertEquals(metricasEsperadas, metricas);
    }
}
