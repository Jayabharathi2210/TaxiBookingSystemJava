import java.util.*;

public class TaxiBooking {

    static int customerCount = 1;
    static List<Taxi> taxis = new ArrayList<>();
    static Scanner in = new Scanner(System.in);
    
    public static void main(String[] args) {

        System.out.println("Enter the number of taxis: ");
        int numTaxis = in.nextInt();
        initializeTaxis(numTaxis);

        while(true)
        {
            System.out.print(" 1.Book Taxi\n 2.View Taxi details\n 3.Exit\n");
            System.out.println("Enter your choice: ");
            int choice = in.nextInt();

            switch(choice)
            {
                case 1:
                bookTaxi();
                break;

                case 2:
                displayDetails();
                break;

                case 3: 
                System.out.println("Exiting...");
                return;

                default:
                System.out.println("Invalid Choice! Try again");
            }
        }
        
    }

    public static void initializeTaxis(int num){
        for(int i=1; i<=num; i++){
            taxis.add(new Taxi(i));
        }
    }

    public static void bookTaxi(){
        int customerId = customerCount++;
        System.out.println("Enter pickup point (A-F): ");
        char pickup = in.next().toUpperCase().charAt(0);
        System.out.println("Enter drop point (A-F): ");
        char drop = in.next().toUpperCase().charAt(0);
        System.out.println("Enter pickup time: ");
        int pickupTime = in.nextInt();

        Taxi selectedTaxi = null;
        int minDistance = Integer.MAX_VALUE;

        for(Taxi taxi : taxis){
            if(taxi.isAvailable(pickupTime)){
                int distance = Math.abs(taxi.currentPoint - pickup);
                if(distance < minDistance || 
                (distance == minDistance && selectedTaxi.totalEarnings > taxi.totalEarnings)){
                    selectedTaxi = taxi;
                    minDistance = distance;
                }
            }
        }
        if(selectedTaxi == null){
            System.out.println("Sorry! Booking rejected. Taxis are not available");
            return;
        }

        int bookingId = selectedTaxi.bookings.size()+1;
        int dropTime = pickupTime + Math.abs(drop - pickup);
        int amount = selectedTaxi.calculateEarnings(pickup, drop);

        Booking booking = new Booking(bookingId, customerId, pickup, drop, pickupTime, dropTime, amount);
        selectedTaxi.addBooking(booking);
        System.out.println("Taxi-"+selectedTaxi.id+" is allocated.");
    }

    public static void displayDetails(){
        for(Taxi taxi : taxis){
            System.out.println("Taxi id - "+taxi.id+" Total Earnings - "+taxi.totalEarnings);
            System.out.printf("%-11s %-12s %-5s %-3s %-12s %-10s %-7s %n",
                    "Booking Id", "Customer Id", "From", "To",
                    "Pickup Time", "Drop Time", "Amount");
            for(Booking booking : taxi.bookings){
                System.out.printf("%-11s %-12s %-5s %-3s %-12s %-10s %-7s %n",
                    booking.bookingId, booking.customerId, booking.from, booking.to, 
                    booking.pickupTime, booking.dropTime, booking.amount);
            }
        }
    }
}
