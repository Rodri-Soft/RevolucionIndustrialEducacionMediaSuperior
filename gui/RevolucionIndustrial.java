package gui;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author daniCV
 */
public class RevolucionIndustrial extends Application implements Initializable {

    @FXML
    private Label labelInvalidDate;

    @FXML
    private Slider sliderTimeLine;

    @FXML
    private TextField textFieldDay;

    @FXML
    private TextField textfieldYear;

    @FXML
    private TextField textFieldMonth;

    @FXML
    private ImageView imageViewSad;

    static Label labelInformation = new Label();
    static ImageView imageViewBoxInformation = new ImageView("/images/box_with_arrow.png");
    static Slider slider = new Slider(1700,2100, 1700);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        validateTextFieldsFormat();
        setMaxLenght(textFieldDay, 2);
        setMaxLenght(textFieldMonth, 2);
        setMaxLenght(textfieldYear, 4);
    }

    public void validateTextFieldsFormat() {

        textFieldDay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldDay.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textFieldMonth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldMonth.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textfieldYear.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textfieldYear.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }

    public static void setMaxLenght(TextField textField, int limit) {

        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter(textLimitFilter));
    }

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLMain.fxml")));

        AnchorPane layout = new AnchorPane(root, labelInformation, imageViewBoxInformation, slider);
        layout.setPrefSize(895, 597);

        slider.setLayoutX(51);
        slider.setLayoutY(400);
        slider.setPrefSize(782, 30);

        imageViewBoxInformation.setFitHeight(189);
        imageViewBoxInformation.setFitWidth(441);
        imageViewBoxInformation.setLayoutX(95);
        imageViewBoxInformation.setLayoutY(388);
        imageViewBoxInformation.setPickOnBounds(true);
        imageViewBoxInformation.setPreserveRatio(true);

        labelInformation.setTextAlignment(TextAlignment.CENTER);
        labelInformation.setFont(new Font("Arial", 10));
        labelInformation.setPrefHeight(137);
        labelInformation.setPrefWidth(227);

        Scene scene = new Scene(layout);
        stage.setTitle("Linea del tiempo: Revolución Industrial");
        stage.getIcons().add(new Image("/images/industrial.png"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        labelInformation.setVisible(false);

        reportThumbBounds(slider, imageViewBoxInformation);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            reportThumbBounds(slider, imageViewBoxInformation);
        });
        slider.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            reportThumbBounds(slider, imageViewBoxInformation);
        });

        labelInformation.setWrapText(true);
        imageViewBoxInformation.setVisible(false);
        slider.setVisible(false);
    }

    private void reportThumbBounds(Slider slider, ImageView imageViewPrueba) {

//        imageViewBoxInformation.setVisible(true);
        labelInformation.setVisible(true);

        Bounds bounds = slider.lookup(".thumb").getBoundsInParent();
        imageViewPrueba.setLayoutX(bounds.getMinX()-58);
        imageViewPrueba.setLayoutY(392);

        labelInformation.setLayoutX(bounds.getMinX()-53);
        labelInformation.setLayoutY(440);

    }

    private void changePosition(Slider slider, double newPosition){
        slider.setValue(newPosition);

    }

    @FXML
    private void clickPlay(ActionEvent actionEvent) {


        if(!(textFieldDay.getText().trim().equals("")) && !(textFieldMonth.getText().trim().equals("")) &&
                !(textfieldYear.getText().trim().equals(""))){

            labelInvalidDate.setVisible(false);
            imageViewSad.setVisible(false);
            validateYear(textfieldYear.getText());

        }else{
            labelInvalidDate.setVisible(true);
            labelInvalidDate.setText("Ups! Ingresa todos los campos");
            setSadImage();
        }
    }

    public void validateYear(String textYear){

        try{

            int year = Integer.parseInt(textYear);
            Date date = new Date();
            if( year>=1760 && year <= (date.getYear()+1900)){

                if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
                    if(year == (date.getYear()+1900)){
                        validateActualMonth(textFieldMonth.getText(), true);
                    }else{
                        validateMonth(textFieldMonth.getText(), true);
                    }

                }else{
                    if(year == (date.getYear()+1900)){
                        validateActualMonth(textFieldMonth.getText(), false);
                    }else{
                        validateMonth(textFieldMonth.getText(), false);
                    }
                }

            }else{
                labelInvalidDate.setVisible(true);
                setSadImage();
                if(year<1760){
                    labelInvalidDate.setText("Ups! La industria todavía no surgía como tal");
                }else if(year>(date.getYear()+1900)){
                    if(year>2100){
                        labelInvalidDate.setText("Ups! Fuera de rango");
                    }else{
                        labelInvalidDate.setText("Ups! No sé adivinar el futuro");
                    }

                }
            }

        }catch (NumberFormatException exception){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Formato incorrecto");
            alert.show();
        }


    }

    public void validateMonth(String textMonth, boolean isLeapYear){
        try{
            if(isLeapYear){

                int month = Integer.parseInt(textMonth);
                if(month >= 1 && month <= 12){

                    if(month == 2){

                        validateDayFebruary(textFieldDay.getText(), true);

                    }else if(month == 1 || month == 3 || month == 5 ||  month == 7 ||
                            month == 8 || month == 10 || month == 12){

                        validateDay(textFieldDay.getText(), true);
                    }else{

                        validateDay(textFieldDay.getText(), false);
                    }

                }else{
                    labelInvalidDate.setVisible(true);
                    labelInvalidDate.setText("Ups! Mes fuera de rango");
                    setSadImage();
                }

            }else{

                int month = Integer.parseInt(textMonth);
                if(month >= 1 && month <= 12){

                    if(month == 2){

                        validateDayFebruary(textFieldDay.getText(), false);

                    }else if(month == 1 || month == 3 || month == 5 ||  month == 7 ||
                            month == 8 || month == 10 || month == 12){

                        validateDay(textFieldDay.getText(), true);
                    }else{

                        validateDay(textFieldDay.getText(), false);
                    }

                }else{
                    labelInvalidDate.setVisible(true);
                    labelInvalidDate.setText("Ups! Mes fuera de rango");
                    setSadImage();
                }

            }
        }catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Formato incorrecto");
            alert.show();
        }

    }

    public void validateDay(String textDay, boolean isLongerMonth){

        try{
            int day = Integer.parseInt(textDay);
            if(isLongerMonth){

                if(day>=1 && day<=31){

                    double positionSlider = Double.parseDouble(textfieldYear.getText());
                    sliderTimeLine.setValue(positionSlider);
                    changePosition(slider, positionSlider);
                    setInformationText();

                }else{
                    labelInvalidDate.setVisible(true);
                    labelInvalidDate.setText("Ups! Día fuera de rango");
                    setSadImage();
                }
            }else{

                if(day>=1 && day<=30){

                    double positionSlider = Double.parseDouble(textfieldYear.getText());
                    sliderTimeLine.setValue(positionSlider);
                    changePosition(slider, positionSlider);
                    setInformationText();

                }else{
                    labelInvalidDate.setVisible(true);
                    labelInvalidDate.setText("Ups! Día fuera de rango");
                    setSadImage();
                }
            }

        }catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Formato incorrecto");
            alert.show();
        }

    }

    public void validateDayFebruary(String textDay, boolean isLeapYear){

        try{
            int day = Integer.parseInt(textDay);
            if(isLeapYear){
                if(day>=1 && day<=29){

                    double positionSlider = Double.parseDouble(textfieldYear.getText());
                    sliderTimeLine.setValue(positionSlider);
                    changePosition(slider, positionSlider);
                    setInformationText();

                }else{
                    labelInvalidDate.setVisible(true);
                    labelInvalidDate.setText("Ups! Día fuera de rango");
                    setSadImage();
                }
            }else{
                if(day>=1 && day<=28){

                    double positionSlider = Double.parseDouble(textfieldYear.getText());
                    sliderTimeLine.setValue(positionSlider);
                    changePosition(slider, positionSlider);
                    setInformationText();

                }else{
                    labelInvalidDate.setVisible(true);
                    labelInvalidDate.setText("Ups! Día fuera de rango");
                    setSadImage();
                }
            }

        }catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Formato incorrecto");
            alert.show();
        }
    }

    public void validateActualMonth(String textMonth, boolean isLeapYear){

        try{

            int month = Integer.parseInt(textMonth);
            Date date = new Date();

            if(isLeapYear){

                if(month >= 1 && month <= date.getMonth()+1){

                    if(month == 2){
                        validateActualDayFebruary(textFieldDay.getText(), true);

                    }else if(month == 1 || month == 3 || month == 5 ||  month == 7 ||
                            month == 8 || month == 10 || month == 12){

                        validateActualDay(textFieldDay.getText(), true);
                    }else{

                        validateActualDay(textFieldDay.getText(), false);
                    }

                }else{
                    labelInvalidDate.setVisible(true);
                    setSadImage();
                    if(month < 1 || month > 12 ){

                        labelInvalidDate.setText("Ups! Mes fuera de rango");

                    }else{
                        labelInvalidDate.setText("Ups! No sé adivinar el futuro");
                    }

                }

            }else{

                if(month >= 1 && month <= date.getMonth()+1){

                    if(month == 2){

                        validateActualDayFebruary(textFieldDay.getText(), false);

                    }else if(month == 1 || month == 3 || month == 5 ||  month == 7 ||
                            month == 8 || month == 10 || month == 12){

                        validateActualDay(textFieldDay.getText(), true);
                    }else{

                        validateActualDay(textFieldDay.getText(), false);
                    }

                }else{
                    labelInvalidDate.setVisible(true);
                    setSadImage();
                    if(month < 1 || month > 12 ){

                        labelInvalidDate.setText("Ups! Mes fuera de rango");

                    }else{
                        labelInvalidDate.setText("Ups! No sé adivinar el futuro");
                    }
                }

            }

        }catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Formato incorrecto");
            alert.show();
        }
    }

    public void validateActualDay(String textDay, boolean isLongerMonth){

        try{
            int month = Integer.parseInt(textFieldMonth.getText());
            int day = Integer.parseInt(textDay);
            Calendar date = new GregorianCalendar();
            Date dateMonth = new Date();
            int actualDay = date.get(Calendar.DAY_OF_MONTH);
            int actualMonth = dateMonth.getMonth()+1;

            if(month != actualMonth){
                if(isLongerMonth){

                    if(day>=1 && day<=31){

                        double positionSlider = Double.parseDouble(textfieldYear.getText());
                        sliderTimeLine.setValue(positionSlider);
                        changePosition(slider, positionSlider);
                        setInformationText();

                    }else{
                        labelInvalidDate.setVisible(true);
                        labelInvalidDate.setText("Ups! Día fuera de rango");
                        setSadImage();
                    }
                }else{

                    if(day>=1 && day<=30){

                        double positionSlider = Double.parseDouble(textfieldYear.getText());
                        sliderTimeLine.setValue(positionSlider);
                        changePosition(slider, positionSlider);
                        setInformationText();

                    }else{
                        labelInvalidDate.setVisible(true);
                        labelInvalidDate.setText("Ups! Día fuera de rango");
                        setSadImage();
                    }
                }
            }else{

                if(isLongerMonth){

                    if(day>=1 && day <= actualDay){

                        imageViewBoxInformation.setVisible(true);
                        labelInformation.setVisible(true);
                        double positionSlider = Double.parseDouble(textfieldYear.getText());
                        sliderTimeLine.setValue(positionSlider);
                        changePosition(slider, positionSlider);
                        setInformationText();

                    }else{

                        labelInvalidDate.setVisible(true);
                        setSadImage();
                        if(day < 1 || day > 31 ){

                            labelInvalidDate.setText("Ups! Día fuera de rango");

                        }else{
                            labelInvalidDate.setText("Ups! No sé adivinar el futuro");
                        }
                    }
                }else{
                    if(day>=1 && day <= actualDay){

                        imageViewBoxInformation.setVisible(true);
                        labelInformation.setVisible(true);
                        double positionSlider = Double.parseDouble(textfieldYear.getText());
                        sliderTimeLine.setValue(positionSlider);
                        changePosition(slider, positionSlider);
                        setInformationText();

                    }else{

                        labelInvalidDate.setVisible(true);
                        setSadImage();
                        if(day < 1 || day > 30 ){

                            labelInvalidDate.setText("Ups! Día fuera de rango");

                        }else{
                            labelInvalidDate.setText("Ups! No sé adivinar el futuro");
                        }
                    }
                }
            }

        }catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Formato incorrecto");
            alert.show();
        }

    }

    public void validateActualDayFebruary(String textDay, boolean isLeapYear){

        try{

            int day = Integer.parseInt(textDay);
            Calendar date = new GregorianCalendar();
            Date dateMonth = new Date();
            int actualDay = date.get(Calendar.DAY_OF_MONTH);
            int actualMonth = dateMonth.getMonth()+1;

            if(actualMonth == 2){
                if(isLeapYear){
                    if(day>=1 && day<=actualDay){

                        double positionSlider = Double.parseDouble(textfieldYear.getText());
                        sliderTimeLine.setValue(positionSlider);
                        changePosition(slider, positionSlider);
                        setInformationText();

                    }else{
                        labelInvalidDate.setVisible(true);
                        setSadImage();
                        if(day < 1 || day > 29 ){

                            labelInvalidDate.setText("Ups! Día fuera de rango");

                        }else{
                            labelInvalidDate.setText("Ups! No sé adivinar el futuro");
                        }
                    }
                }else{

                    if(day>=1 && day<=actualDay){

                        double positionSlider = Double.parseDouble(textfieldYear.getText());
                        sliderTimeLine.setValue(positionSlider);
                        changePosition(slider, positionSlider);
                        setInformationText();

                    }else{
                        labelInvalidDate.setVisible(true);
                        setSadImage();
                        if(day < 1 || day > 28 ){

                            labelInvalidDate.setText("Ups! Día fuera de rango");

                        }else{
                            labelInvalidDate.setText("Ups! No sé adivinar el futuro");
                        }
                    }
                }
            }else{

                if(isLeapYear){
                    validateDayFebruary(textFieldDay.getText(), true);
                }else{
                    validateDayFebruary(textFieldDay.getText(), false);
                }

            }

        }catch (NumberFormatException exception){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Formato incorrecto");
            alert.show();
        }
    }


    public void setInformationText(){

        imageViewBoxInformation.setVisible(true);
        labelInformation.setVisible(true);
        int year = Integer.parseInt(textfieldYear.getText());
        if(year>=1760 && year<=1830){
            labelInformation.setText("Un día del período de la Industria 1.0 que se caracterizó por la predominancia de" +
                    " las industrias metalúrgica, textil y por el ferrocarril como principal medio de transporte, que " +
                    "utilizaba el carbón como fuente de energía. Por su parte, el telégrafo y el teléfono revolucionaron" +
                    " la forma en la que las comunicaciones eran concebidas hasta ese momento.");
        }else if(year>=1850 && year<=1914){
            labelInformation.setText("Un día del período de la Industria 2.0 el cual supuso el desarrollo de las " +
                    "industrias química, eléctrica y automovilística. El coche y más tarde el avión, nacieron al " +
                    "albor de los cambios en este segundo periodo, que se extendió durante más de un siglo. Estos " +
                    "medios cambiaron el carbón por el petróleo como fuente de energía.");
        }else if(year>=1960 && year<=1970){
            labelInformation.setText("Un día del período de la Industria 3.0 caracterizada por el uso de la electrónica," +
                    " las tecnologías de la información y las telecomunicaciones. Estos cambios permitieron la " +
                    "automatización de los procesos de producción y el surgimiento de Internet, que sin duda, ha supuesto " +
                    "una importante innovación en nuestro modo de ver y entender la vida y sobre todo la comunicación. Las" +
                    " energías alternativas y renovables, la nuclear y el petróleo se han erigido durante estos años como las" +
                    " principales fuentes de energía.");
        }else{
            Date date = new Date();
            if(year>=2011 && year <= (date.getYear()+1900)){
                labelInformation.setText("Un día del período de la Industria 4.0, esta nueva revolución no ha hecho sino" +
                        " multiplicar la velocidad, el alcance y el impacto de las herramientas que surgieron en el periodo" +
                        " anterior, mediante la conexión de los mundos digital, físico y biológico. Fábricas inteligentes," +
                        " lugares de producción en los que los dispositivos están conectados entre sí con el objetivo de" +
                        " difuminar las barreras entre la demanda, el diseño, la fabricación y el suministro.");
            }else{
                labelInformation.setText("Período de transición entre evoluciones industriales");
            }
        }
    }

    public void setSadImage(){
        sliderTimeLine.setValue(1700);
        imageViewSad.setVisible(true);
        imageViewBoxInformation.setVisible(false);
        labelInformation.setVisible(false);
    }


    public static void main(String[] args) {
        launch(args);
    }


}
