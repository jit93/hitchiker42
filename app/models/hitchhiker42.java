package models;

import play.db.*;
import java.sql.*;
import javax.sql.*;
import java.util.ArrayList;

public class hitchhiker42 {

    //TRIPS IS NOT A VARIABLE TYPE?
    public static TripInfo getTrips(String t_id, String dep, String arr, String startd1, String startd2) throws SQLException {
         Connection connection = null;
         TripInfo trips = null;
         try {
             connection = DB.getConnection();
             // retrieve basic info:
	ResultSet rs;
	PreparedStatement statement;

	if(!startd1.equals("*")){
             statement = connection
                 .prepareStatement("SELECT * FROM trips WHERE estimated_start_date_time >= ? AND estimated_start_date_time <= ?");
             statement.setString(1, startd1);
             statement.setString(2, startd2);
             rs = statement.executeQuery();	
	}
	else{
             int t_id_int = Integer.parseInt(t_id);
	     statement = connection
                 .prepareStatement("SELECT * FROM trips WHERE trip_id = ?");
             statement.setInt(1, t_id_int);
             rs = statement.executeQuery();	
		}

             ArrayList<Integer> trip_ids = new ArrayList<Integer>();
             ArrayList<String> curs = new ArrayList<String>();
             ArrayList<String> dests = new ArrayList<String>();            
             ArrayList<String> s_ds = new ArrayList<String>();
             while (rs.next()){
                  System.out.println("******************************** in result.rs");
                 Integer trip_id = rs.getInt(1);
                 trip_ids.add(trip_id);
                 String cur = rs.getString(2);
                 curs.add(cur);
                 String dest = rs.getString(3);
                 dests.add(dest);
                 String s_d = rs.getString(4);
                 s_ds.add(s_d);
             }
             rs.close();
             statement.close();            
             trips = new TripInfo(trip_ids, curs, dests, s_ds);
         } finally {
             if (connection != null) {
                try {
                     connection.close();
                 } catch (Exception e) {
                 }
             }
         }
         return trips;
     }
//--------------------------------------------------------------------------------------------------------------------------------
    public static boolean signIn(String email, String password) throws SQLException {
        Connection connection = null;
        boolean valid = true;
        try {
            connection = DB.getConnection();
            PreparedStatement statement = connection
                .prepareStatement("SELECT email, passwordHash FROM users WHERE email = ? AND passwordHash = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (! rs.next()) {
                return false;
            }

            rs.close();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }


// //--------------------------------------------------------------------------------------------------------------------------------
     public static boolean insertUserInfo(String email, String name, String passwordHash) throws SQLException{
         //email, name, password
         Connection connection = null;
         try {
             connection = DB.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement("INSERT INTO users VALUES(?, ?, ?)");
                 statement.setString(1, email);
                 statement.setString(2, name);
                 statement.setString(3, passwordHash);
                 statement.executeUpdate();
             statement.close();
         } finally {
             if (connection != null) {
                 try {
                     connection.close();
                 } catch (Exception e) {
                 }
             }
         }
         return true;
     }
// //--------------------------------------------------------------------------------------------------------------------------------
         public static boolean updateUserInfo(String email, String newName, String newPasswordHash, boolean delete) throws SQLException{
         //email, name, password
         Connection connection = null;
         try {
             connection = DB.getConnection();
             if(!delete){
                 PreparedStatement statement = connection
                 .prepareStatement("UPDATE users SET name = ?, passwordHash = ? WHERE email = ?");
             statement.setString(1, newName);
             statement.setString(2, newPasswordHash);
             statement.setString(3, email);
             statement.executeUpdate();
             statement.close();
             }
             else{
                 PreparedStatement statement = connection
                 .prepareStatement("DELETE FROM users WHERE email = ?");
             statement.setString(1, email);
             statement.executeUpdate();
             statement.close();
             }
            
         } finally {
             if (connection != null) {
                 try {
                     connection.close();
                 } catch (Exception e) {
                 }
             }
         }
         return true;
     }
// //--------------------------------------------------------------------------------------------------------------------------------
    public static boolean insertUsersWithCar(String email, int numseats) throws SQLException{
        Connection connection = null;
        try {
            connection = DB.getConnection();
            PreparedStatement statement = connection
                .prepareStatement("INSERT INTO usersWithCar VALUES(?, ?)");
                statement.setString(1, email);
                statement.setInt(2, numseats);
                statement.executeUpdate();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }

    public static ArrayList<String> getUserWithCar(String email) throws SQLException{
        Connection connection = null;
        ArrayList<String> users = new ArrayList<String>();
        try {
            connection = DB.getConnection();
            
            PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM usersWithCar WHERE email = ?");
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();
                 while (rs.next()){
                    String user = rs.getString(1) + ", " + rs.getString(2);
                    users.add(user);
                 }
                 rs.close();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        if(users.size() == 0){
            users.add("., .");
        }
        return users;
    }
// //--------------------------------------------------------------------------------------------------------------------------------
    public static boolean updateUsersWithCar(String email, int numSeats, boolean delete) throws SQLException{
        Connection connection = null;
        try {
            connection = DB.getConnection();

            if(!delete){
                PreparedStatement statement = connection
                .prepareStatement("UPDATE usersWithCar SET numSeats = ? WHERE email = ?");
                statement.setInt(1, numSeats);
                statement.setString(2, email);
                statement.executeUpdate();
                statement.close();
            }
            else{
                PreparedStatement statement = connection
                .prepareStatement("DELETE FROM usersWithCar WHERE email = ?");
                statement.setString(1, email);
                statement.executeUpdate();
                statement.close();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
// //--------------------------------------------------------------------------------------------------------------------------------
    public static boolean insertIntoPassengers(int trip_id, String email) throws SQLException{
        Connection connection = null;
        try {
            connection = DB.getConnection();
            PreparedStatement statement = connection
                .prepareStatement("INSERT INTO passengers VALUES(?, ?)");
                statement.setInt(1, trip_id);
                statement.setString(2, email);
                statement.executeUpdate();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
// //--------------------------------------------------------------------------------------------------------------------------------
    public static boolean updatePassengers(int trip_id, String email, boolean delete) throws SQLException{
        Connection connection = null;
        try {
            connection = DB.getConnection();
            
            if(!delete){
                PreparedStatement statement = connection
                .prepareStatement("UPDATE passengers SET trip_id = ? WHERE email = ?");
                statement.setInt(1, trip_id);
                statement.setString(2, email);
                statement.executeUpdate();
                statement.close();
            }
            else{
                PreparedStatement statement = connection
                .prepareStatement("DELETE FROM passengers WHERE trip_id = ? AND email = ?");
                statement.setInt(1, trip_id);
                statement.setString(2, email);
                statement.executeUpdate();
                statement.close();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
// //--------------------------------------------------------------------------------------------------------------------------------
    public static boolean insertIntoIsDrivenBy(int trip_id, String email) throws SQLException{
        Connection connection = null;
        try {
            connection = DB.getConnection();            
            PreparedStatement statement = connection
                .prepareStatement("INSERT INTO isDrivenBy VALUES(?, ?)");
                statement.setInt(1, trip_id);
                statement.setString(2, email);
                statement.executeUpdate();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
// //--------------------------------------------------------------------------------------------------------------------------------
    public static boolean updateIsDrivenBy(int trip_id, String email, boolean delete) throws SQLException{
        Connection connection = null;
        try {
            connection = DB.getConnection();            
            if(!delete){
                PreparedStatement statement = connection
                .prepareStatement("UPDATE isDrivenBy SET email = ? WHERE trip_id = ?");
                statement.setString(1, email);
                statement.setInt(2, trip_id);

                statement.executeUpdate();
                statement.close();
            }
            else{
                PreparedStatement statement = connection
                .prepareStatement("DELETE FROM isDrivenBy WHERE trip_id = ?");
                statement.setInt(1, trip_id);
                statement.executeUpdate();
                statement.close();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
// //---------------------------------------------------------------------------------------------------------------


    public static boolean insertIntoTrips(TripInfo tripInfo)
        throws SQLException {
        Connection connection = null;
        boolean success = false;
        try{
            connection = DB.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO trips VALUES(?,?,?,?)");
            statement.setInt(1, tripInfo.trip_ids.get(0));
            statement.setString(2, tripInfo.depart_locs.get(0));
            statement.setString(3, tripInfo.arrive_locs.get(0));
            statement.setString(4, tripInfo.depart_times.get(0));
            statement.executeUpdate();
            statement.close();
        } finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return success;
    }


    public static boolean updateTripInfo(TripInfo tripInfo, boolean delete)
        throws SQLException {
        Connection connection = null;
        boolean success = false;
        try{
            connection = DB.getConnection();
            if (delete){
                PreparedStatement statement = connection.prepareStatement("DELETE FROM trips WHERE trip_id = ?");
                statement.setInt(1, tripInfo.trip_ids.get(0));
                statement.executeUpdate();
                statement.close();
            }
            else{
            PreparedStatement statement = connection.prepareStatement("UPDATE trips SET destination = ?, current_location = ?, estimated_start_date_time = ? WHERE trip_id = ?");
            statement.setString(1, tripInfo.arrive_locs.get(0));
            statement.setString(2, tripInfo.depart_locs.get(0));
            statement.setString(3, tripInfo.depart_times.get(0));
            statement.setInt(4, tripInfo.trip_ids.get(0));
            success = (statement.executeUpdate() == 1);
            statement.close();
            if (! success){
                return false;
            }    
            }
        } finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return success;
    }




    public static String getDriver(String trip_id) throws SQLException{
        Connection connection = null;
        String driverEmail = "";
        int trip_id_int = Integer.parseInt(trip_id);
        System.out.println("in hitchhiker****************************************");
        try {
            connection = DB.getConnection();
            
            PreparedStatement statement = connection
                .prepareStatement("SELECT email FROM isDrivenBy WHERE trip_id = ?");
            statement.setInt(1, trip_id_int);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                driverEmail = rs.getString(1);
            }
            rs.close();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return driverEmail;
    }


     // public static boolean signIn(String em, String pass) throws SQLException {
     //     Connection connection = null;
     //     try {
     //         connection = DB.getConnection();
     //         PreparedStatement statement = connection
     //             .prepareStatement("SELECT email, passwordHash FROM users WHERE email = ? AND passwordHash = ?");
     //         statement.setString(1, em);
     //         statement.setString(2, pass);
     //         ResultSet rs = statement.executeQuery();
     //         if (! rs.next()) {
     //             return false;
     //         }


     //         rs.close();
     //         statement.close();
     //     } finally {
     //         if (connection != null) {
     //             try {
     //                 connection.close();
     //             } catch (Exception e) {
     //             }
     //         }
     //     }
     //     return true;
     // }

    public static ArrayList<String> getPassengers(String trip_id) throws SQLException{
        Connection connection = null;
        ArrayList<String> passengerEmails = new ArrayList<String>();
        try {
            connection = DB.getConnection();
            int trip_id_int = Integer.parseInt(trip_id);

            PreparedStatement statement = connection
                .prepareStatement("SELECT email FROM passengers WHERE trip_id = ?");
            statement.setInt(1, trip_id_int);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String passengerEmail = rs.getString(1);
                passengerEmails.add(passengerEmail);
            }
            rs.close();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return passengerEmails;
    }




    public static ArrayList<String> getUserInfo(String email) throws SQLException{
        Connection connection = null;
        ArrayList<String> userInfo = new ArrayList<String>();
        try {
            connection = DB.getConnection();
            
            PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //String information = rs.getString(1);
                String information = rs.getString(1) +", ";
                information += rs.getString(2) + ", ";
                information += rs.getString(3);
                userInfo.add(information);
            }
            rs.close();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return userInfo;
    }

      public static class TripInfo {

         public ArrayList<Integer> trip_ids = null;
         public ArrayList<String> depart_locs = null;
         public ArrayList<String> arrive_locs = null;
         public ArrayList<String> depart_times = null;
         public TripInfo() {
         }
         public TripInfo(ArrayList<Integer> trip_ids,
         	ArrayList<String> depart_locs,
         	ArrayList<String> arrive_locs,
         	ArrayList<String> depart_times) {
             this.trip_ids = trip_ids;
             this.depart_locs = depart_locs;
             this.arrive_locs = arrive_locs;
             this.depart_times = depart_times;
         }
     }
    
    public static class DrinkerInfo {
        public String name = null;
        public String address = null;
        public ArrayList<String> beersLiked = null;
        public ArrayList<String> barsFrequented = null;
        public ArrayList<Integer> timesFrequented = null;
        public DrinkerInfo() {
        }
        public DrinkerInfo(String name,
                           String address,
                           ArrayList<String> beersLiked,
                           ArrayList<String> barsFrequented,
                           ArrayList<Integer> timesFrequented) {
            this.name = name;
            this.address = address;
            this.beersLiked = beersLiked;
            this.barsFrequented = barsFrequented;
            this.timesFrequented = timesFrequented;
        }
    }

    public static ArrayList<String> getAllNames(String table) throws SQLException {
        assert table.equals("drinker")
            || table.equals("beer")
            || table.equals("bar");
        Connection connection = null;
        ArrayList<String> names = new ArrayList<String>();
        try {
            connection = DB.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement
                .executeQuery("SELECT name FROM " + table + " ORDER BY name");
            while (rs.next()) {
                String name = rs.getString(1);
                names.add(name);
            }
            rs.close();
            statement.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return names;
    }

    public static ArrayList<String> getAllDrinkerNames() throws SQLException {
        return getAllNames("drinker");
    }

    public static ArrayList<String> getAllBeerNames() throws SQLException {
        return getAllNames("beer");
    }

    public static ArrayList<String> getAllBarNames() throws SQLException {
        return getAllNames("bar");
    }

    public static DrinkerInfo getDrinkerInfo(String name) throws SQLException {
        Connection connection = null;
        DrinkerInfo drinkerInfo = null;
        try {
            connection = DB.getConnection();
            // retrieve basic info:
            PreparedStatement statement = connection
                .prepareStatement("SELECT address FROM drinker WHERE name = ?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (! rs.next()) {
                return null;
            }
            String address = rs.getString(1);
            rs.close();
            statement.close();
            // retrieve beers liked:
            statement = connection
                .prepareStatement("SELECT beer FROM Likes WHERE drinker =?");
            statement.setString(1, name);
            rs = statement.executeQuery();
            ArrayList<String> beers = new ArrayList<String>();
            while (rs.next()) {
                String beer = rs.getString(1);
                beers.add(beer);
            }
            rs.close();
            statement.close();
            // retrieve bars frequented:
            statement = connection
                .prepareStatement("SELECT bar, times_a_week" +
                                  " FROM Frequents" +
                                  " WHERE drinker = ?");
            statement.setString(1, name);
            rs = statement.executeQuery();
            ArrayList<String> bars = new ArrayList<String>();
            ArrayList<Integer> times = new ArrayList<Integer>();
            while (rs.next()) {
                String bar = rs.getString(1);
                bars.add(bar);
                int times_a_week = rs.getInt(2);
                times.add(times_a_week);
            }
            rs.close();
            statement.close();
            drinkerInfo = new DrinkerInfo(name, address, beers, bars, times);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return drinkerInfo;
    }

    // public static boolean updateDrinkerInfo(DrinkerInfo drinkerInfo)
    //     throws SQLException {
    //     Connection connection = null;
    //     boolean oldAutoCommitState = true;
    //     boolean success = false;
    //     try {
    //         connection = DB.getConnection();
    //         oldAutoCommitState = connection.getAutoCommit();
    //         connection.setAutoCommit(false);
    //         // update basic info:
    //         PreparedStatement statement = connection
    //             .prepareStatement("UPDATE drinker SET address = ? WHERE name = ?");
    //         statement.setString(1, drinkerInfo.address);
    //         statement.setString(2, drinkerInfo.name);
    //         success = (statement.executeUpdate() == 1);
    //         statement.close();
    //         if (! success) {
    //             connection.rollback();
    //             return false;
    //         }
    //         // delete old beers liked:
    //         statement = connection
    //             .prepareStatement("DELETE FROM Likes WHERE drinker = ?");
    //         statement.setString(1, drinkerInfo.name);
    //         statement.executeUpdate();
    //         statement.close();
    //         // add new beers liked:
    //         statement = connection
    //             .prepareStatement("INSERT INTO Likes VALUES(?, ?)");
    //         for (String beer: drinkerInfo.beersLiked) {
    //             statement.setString(1, drinkerInfo.name);
    //             statement.setString(2, beer);
    //             statement.executeUpdate();
    //         }
    //         statement.close();
    //         // delete old bars frequented:
    //         statement = connection
    //             .prepareStatement("DELETE FROM Frequents WHERE drinker = ?");
    //         statement.setString(1, drinkerInfo.name);
    //         statement.executeUpdate();
    //         statement.close();
    //         // add new bars frequented:
    //         statement = connection
    //             .prepareStatement("INSERT INTO Frequents VALUES(?, ?, ?)");
    //         for (int i=0; i<drinkerInfo.barsFrequented.size(); i++) {
    //             statement.setString(1, drinkerInfo.name);
    //             statement.setString(2, drinkerInfo.barsFrequented.get(i));
    //             statement.setInt(3, drinkerInfo.timesFrequented.get(i));
    //             statement.executeUpdate();
    //         }
    //         statement.close();
    //         connection.commit();
    //     } finally {
    //         if (connection != null) {
    //             try {
    //                 connection.setAutoCommit(oldAutoCommitState);
    //                 connection.close();
    //             } catch (Exception e) {
    //             }
    //         }
    //     }
    //     return success;
    // }

}
