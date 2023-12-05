import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main{

    private static final String url = "jdbc:mysql://localhost:3306/hotal_db";
    private static final String userName = "root";
    private  static final  String passWord = "";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try{
            Connection con = DriverManager.getConnection(url,userName,passWord);
            Statement smt = con.createStatement();

            while(true){

                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = sc.nextInt();

                switch(choice){
                    case 1: reserveRoom(con,smt,sc);
                        break;

                    case 2: viewReservation(con,smt);
                        break;

                    case 3: getRoomNumber(con,smt,sc);
                        break;

                    case 4: updateReservation(con,smt,sc);
                        break;

                    case 5: delteReservation(con,smt,sc);
                        break;

                    case 0 : exit();
                    sc.close();
                    return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection con, Statement smt, Scanner sc){
        System.out.println("Enter your Name ");
        String guestName = sc.next();
        sc.nextLine();

        System.out.println("Enter your Room Number ");
        int roomNumber = sc.nextInt();

        System.out.println("Enter your Contact Number ");
        String contactNumber = sc.next();

        String sql = "INSERT INTO reservations (guest_name,room_number,contact_number) VALUES ("+ "'" + guestName + "'," + roomNumber + ",'" +contactNumber + "')";
        try{
            smt = con.createStatement();
            int rowAffacted = smt.executeUpdate(sql);

            if(rowAffacted > 0){
                System.out.println("Reservation is sussesfull!");
            }else{
                System.out.println("Resercation is failed ");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void viewReservation(Connection con, Statement smt){
        String sql = "SELECT * FROM reservations ;";

        try{
            smt = con.createStatement();
            ResultSet rs = smt.executeQuery(sql);

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while(rs.next()){
                int id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int roomNo = rs.getInt("room_number");
                String contact = rs.getString("contact_number");
                String date = rs.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        id, name, roomNo, contact, date);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void getRoomNumber(Connection con, Statement smt,Scanner sc){
        System.out.println("Enter your reservation id");
        int id = sc.nextInt();
        System.out.println("Enter your Name");
        String name = sc.next();

        String sql = "SELECT room_number FROM reservations " +
                "WHERE reservation_id = " + id +
                " AND guest_name = '" + name + "'";
        try{
            smt = con.createStatement();
            ResultSet rs = smt.executeQuery(sql);

            if(rs.next()){
                System.out.println("Room Number for Guest "+ name +" is "+ rs.getString("room_number"));
            }else{
                System.out.println("Reservation not found for the given ID and guest name.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection con,Statement smt,Scanner sc){

        System.out.println("Enter your RESERVATION ID");
        int Id = sc.nextInt();

        if(!reservationExist(con,smt,Id)){
            System.out.println("Reservation not found for the given ID.");
            return;
        }

        System.out.println("Enter your name to update ");
        String newGuestName = sc.next();
        sc.nextLine();

        System.out.println("Enter your contact Number to update ");
        String newContact = sc.nextLine();

        String sql = "UPDATE reservations "+
                    "SET guest_name = '" + newGuestName + "',"+ "contact_number = '" + newContact + "'" +
                    "WHERE reservation_id = " + Id;

        try{
            smt = con.createStatement();
            int rowAffacted = smt.executeUpdate(sql);

            if(rowAffacted > 0){
                System.out.println("Reservation updated successfully!");

            }else{
                System.out.println("Reservation update failed.");

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void delteReservation(Connection con, Statement smt,Scanner sc){
        System.out.println("Enter your RESERVATION ID");
        int id= sc.nextInt();

        if(!reservationExist(con,smt,id)){
            System.out.println("Reservation not found for the given ID.");
            return;
        }

        String sql = "DELETE from reservations WHERE reservation_id = "+ id;

        try{
            smt = con.createStatement();
            int rowAffacted = smt.executeUpdate(sql);

            if(rowAffacted > 0){
                System.out.println("Reservation deleted successfully!");
            }else{
                System.out.println("Reservation deletion failed.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }


    private static boolean reservationExist(Connection con, Statement smt, int reservation_id){
        String sql = "SELECT * FROM reservations WHERE reservation_id = " + reservation_id;

        try{
            smt = con.createStatement();
            ResultSet rs = smt.executeQuery(sql);

            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
