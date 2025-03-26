package pojo;

import utils.TestData;

public class BookingDates {
    private String checkin;
    private String checkout;

    // Constructor with DataFaker
    public BookingDates() {
        this.checkin = TestData.getCheckInDate();
        this.checkout = TestData.getCheckOutDate();
    }

    // Extra constructor for datatable
    public BookingDates(String checkin, String checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    // Getters and Setters
    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    @Override
    public String toString() {
        return "BookingDates{" +
                "checkin='" + checkin + '\'' +
                ", checkout='" + checkout + '\'' +
                '}';
    }
}
