package connecttodb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class admin {

    public static Statement statement;

	public static void getAdminMenu(Connection connection)   throws SQLException 
	  {
		  	Scanner sc = new Scanner(System.in);
	        int enteredValue = 0;
	 		ResultSet rs = null;
	        statement = connection.createStatement();
	        int input;
	        do {
	            System.out.println("");
	            System.out.println("Select from the options below");
	            System.out.println("1.  Record Label");  //enter-update
	            System.out.println("2.  Podcast");  //enter-update
	            System.out.println("3.  User");  //enter-update
	            System.out.println("4.  Reports");  //enter-update
	            System.out.println("5.  Payments");  //enter-update
	            System.out.println("0.  Go to previous menu");  //enter-update
	            System.out.println("");


	            try {
	                enteredValue = sc.nextInt();

	                switch (enteredValue) {
	                    case 1:
	                    	do {
	                    	System.out.println("");
	        	            System.out.println("Select from the options below");
	        	            System.out.println("1.  Add Record Label");  //enter-update
	        	            System.out.println("2.  Choose Existing Record Label");  //enter-update
	        	            System.out.println("0.  Go to previous menu");  //enter-update

	        	            input = sc.nextInt();
	        	            switch(input)
	        	            {
	        	            case 0:
	        	            	admin.getAdminMenu(connection);
	        	            	break;
	        	            case 1:
	        	            	RL.add_RL(connection);
	        	            	break;
	        	            case 2:
		                    	RL.rl_page(connection);
		                    	break;
	        	            }
	                    	}while(input!=0);
	        	            
//	                    	break;
	                    case 2:
	                    	PM.get_pm_menu(connection);
	                    	break;
	                    case 3:
	                    	do
	                    	{
	                    	System.out.println("");
	        	            System.out.println("Select from the options below");
	        	            System.out.println("1.  Add User");  //enter-update
	        	            System.out.println("2.  Update User info");  //enter-update
	        	            System.out.println("3.  Delete User info");  //enter-update
	        	            System.out.println("4.  View all Users");  //enter-update
	        	            System.out.println("5.  Activate User");  //enter-update
	        	            System.out.println("0.  Go to previous menu");  //enter-update


	        	            String u_email_id;
	    	                input = sc.nextInt();
	        	            User.makeinactive(connection);

	    	                switch(input)
	    	                {
	    	                case 1:
		                    	User.addusers(connection);
		        	            User.makeinactive(connection);

		                    	break;
	    	                case 2:
	    	                	 do {
			        		            System.out.println("");
			        		            System.out.println(" Enter User Email");
			        		            try {
			        		            	u_email_id = sc.next();
			        		            	
			        		                    String x = "select u_email_id AS u_email_id from User where u_email_id='" + u_email_id+"'";
			        		                    ResultSet rs4 = statement.executeQuery(x);
			        		                    
			        		                    if (rs4.next()) {
			        	    	                	User.update_user_info(u_email_id, connection);
			        		                        break;
			        		                    } 
			        		                    else
			        		                    {
						        		            System.out.println(" Try again");

			        		                    }
			        		                } catch (SQLException e) {
			        		                    System.out.println("Could not fetch details");
			        		                }
			        		        } while (true);

	    	                	break;
	    	                case 3:
	    	                	    do {
			        		            System.out.println("");
			        		            System.out.println(" Enter User Email");
			        		            try {
			        		            	u_email_id = sc.next();
			        		            	
			        		                    String x = "select u_email_id AS u_email_id from User where u_email_id='" + u_email_id+"'";
			        		                    ResultSet rs4 = statement.executeQuery(x);
			        		                    
			        		                    if (rs4.next()) {
			       		    	                 User.delete_user_info(u_email_id, connection);
			        		                        break;
			        		                    } 
			        		                    else
			        		                    {
						        		            System.out.println(" Try again");

			        		                    }
			        		                } catch (SQLException e) {
			        		                    System.out.println("Could not fetch details");
			        		                }
			        		        } while (true);
			        		        
			        		        
			        		        
			        		        
		    	                
	    	                	break;
	    	                case 4:
		                    	User.viewusers(connection);
		                    	break;
	    	                case 5:
	    	                	User.activateuser(connection);
	    	                	break;
	    	                case 0:
		                    	admin.getAdminMenu(connection);
		                    	break;
	    	                
	    	                	
	    	                }
	                    	}while(input!=0);
	                    
	                    case 4:
	                    	reports.getreportsmenu(connection);
	                    	break;
	                    	
	                    case 5:
	                    	payments.getpaymentmenu(connection);
	                    	break;
	                    case 0:
	                    	MainMenu.displayMenu(connection);
	                    	break;
	                    default:
	                        System.out.println("You have made an invalid choice. Please pick agai.");
	                }
	            } catch (Exception e) {
	                System.out.println("You have made an invalid choice. Please pick again.");
	            }
	        } while (enteredValue != 0 );
	        
	        getAdminMenu(connection);
	  }

    


}
