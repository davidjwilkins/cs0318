/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
/**
 *
 * @author david.wilkins
 */
public class MainController extends SceneChangerController implements Initializable {
    protected ResourceBundle rb;
    @FXML
    private Button addAppointmentButton, addCustomerButton;
    
    @FXML private GridPane calendarGrid;
    
    @FXML private Label yearLabel, monthLabel;
    
    @FXML
    private ToggleGroup viewType;
    
    private Date calendarDate;
    private String view = "";

    @FXML
    private void addAppointmentAction(ActionEvent event) {
        Context.getInstance().setAppointment(new Appointment());
        this.setScene("Appointment");
    }
    
    @FXML
    private void addCustomerAction(ActionEvent event) {
        Context.getInstance().setCustomer(new Customer());
        this.setScene("Customer");
    }
    
    @FXML
    private void incrementMonth(ActionEvent event) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(calendarDate);
        if (view.equals(rb.getString("couldNotSaveCustomer"))) {
            calendar.add(Calendar.MONTH, 1);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendarDate = calendar.getTime();
        drawCalendar();
    }
    
    @FXML
    private void decrementMonth(ActionEvent event) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(calendarDate);
        if (view.equals(rb.getString("month"))) {
            calendar.add(Calendar.MONTH, -1);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendarDate = calendar.getTime();
        drawCalendar();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        this.calendarDate = new Date();
        viewType.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (viewType.getSelectedToggle() != null) {
                ToggleButton b = (ToggleButton) viewType.getSelectedToggle();
                view = b.getText();
                drawCalendar();
            }
        });
    }
    
    protected Callback<ListView<Appointment>, ListCell<Appointment>> cellRenderer() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
        
        return (ListView<Appointment> param) -> {
            ListCell<Appointment> cell = new ListCell<Appointment>() {
                
                @Override
                protected void updateItem(Appointment item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        String start = formatter.format(item.getStart());
                        String end = formatter.format(item.getEnd());
                        setText("(" + start + " - " + end + ") " + item.getTitle());
                    } else {
                        setText("");
                    }
                }
            };
            return cell;
        };
    }
    
    public void drawCalendar() {
        Callback cellRenderer = this.cellRenderer();
        Date date = calendarDate;
        yearLabel.setText(new SimpleDateFormat("YYYY").format(date));
        monthLabel.setText(new SimpleDateFormat("MMMM").format(date));
        User user = Context.getInstance().getUser();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (view.equals(rb.getString("month"))) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, 7);
        }
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dayOfWeek = new SimpleDateFormat("u");
        SimpleDateFormat dayOfMonth = new SimpleDateFormat("dd");
        SimpleDateFormat dayName = new SimpleDateFormat("E");
        SimpleDateFormat weekDayFormatter = new SimpleDateFormat("E dd");
        int dayNumber = Integer.parseInt(dayOfWeek.format(calendar.getTime()));
        int numberOfBlanks = dayNumber % 7;
        if (calendarGrid.getChildren().size() > 0) {
            calendarGrid.getChildren().remove(0, calendarGrid.getChildren().size());
        }
        Calendar c = (Calendar) calendar.clone();
        c.set(Calendar.DAY_OF_WEEK, 1);
        if (view.equals(rb.getString("month"))) {
            for (int i = 0; i < 7; i++) {
                Label dayLabel = new Label();
                dayLabel.setText(dayName.format(c.getTime()));
                c.add(Calendar.DAY_OF_MONTH, 1);
                calendarGrid.add(dayLabel, i, 0);
            }
            for (int i = numberOfBlanks; i < daysInMonth + numberOfBlanks; i++) {
                int row = i / 7;
                int col = i % 7;
                GridPane pane = new GridPane();
                Label dayLabel = new Label();
                dayLabel.setText(dayOfMonth.format(calendar.getTime()));
                pane.add(dayLabel, 0, 0);
                ListView<Appointment> list = new ListView<>();
                list.setCellFactory(cellRenderer);
                list.setItems(user.getAppointments(calendar.getTime()));

                pane.add(list, 0, 1);
                calendarGrid.add(pane, col, row + 1);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            for (int i = 0; i < 7; i++) {
                Label dayLabel = new Label();
                dayLabel.setText(weekDayFormatter.format(c.getTime()));
                ListView<Appointment> list = new ListView<>();
                list.setCellFactory(cellRenderer);
                list.setItems(user.getAppointments(c.getTime()));
                c.add(Calendar.DAY_OF_MONTH, 1);
                calendarGrid.add(dayLabel, i, 0);
                calendarGrid.add(list, i, 1);
            }
        }
    }
    
    @Override
    protected void refresh() {
        drawCalendar();
    }
}
