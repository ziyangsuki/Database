package youbook.servlet;


import youbook.dao.*;
import youbook.model.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/rentalcreate")
public class RentalCreate extends HttpServlet {
	
	protected RentalDao rentalDao;
	protected UserDao userDao;
	protected BookDao bookDao;
	
	@Override
	public void init() throws ServletException {
		rentalDao = RentalDao.getInstance();
		userDao = UserDao.getInstance();
		bookDao = BookDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        String userName = req.getParameter("username");
        
        if (userName == null || userName.trim().isEmpty()) {
            messages.put("success", "Invalid UserName");
        } else {
        	// Create the BlogUser.
        	int bookId = Integer.parseInt(req.getParameter("bookId"));
        	// dob must be in the format yyyy-mm-dd.
        	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	
        	
        	try {
        		long millis=System.currentTimeMillis();
        		Date date = new Date(millis);
        		User user = userDao.getUserByUserName(userName);
        		Book book = bookDao.getBookById(bookId);
        		Rental rental = new Rental(book, user, date);
        		rental = rentalDao.create(rental);
        		messages.put("success", "Successfully created rental for " + userName);
        	} catch (SQLException e) {
        		e.printStackTrace();
				throw new IOException(e);
        	}
	        messages.put("success", "Successfully created " + userName);
        }
        
        try {

            List<Rental>rentals = rentalDao.getRentalsByUserName(userName);

            req.setAttribute("rentals", rentals);

        } catch (SQLException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }
        
        //Just render the JSP. 
        req.getRequestDispatcher("/RentalView.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    }
}

