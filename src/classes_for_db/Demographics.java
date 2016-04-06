package classes_for_db;

import java.math.BigInteger;
import java.util.List;

// author: ronfarizon

public class Demographics implements DbTableObject {
	
	// 15 Major fields noted
	
	private int zip; //MAJOR
	private int pop; //MAJOR
	
	// Race & Age
	private double pct_female;
	private double median_age;  //MAJOR
	private int pop_18;
	private double pct_female_pop_18;
	private int pop_65;
	private double pct_female_pop_65;
	private int white_pop;  //MAJOR
	private double pct_white_pop;
	private int black_pop; //MAJOR
	private double pct_black_pop;
	private int native_pop; 
	private double pct_native_pop;
	private int asian_pop; //MAJOR
	private double pct_asian_pop;
	private int pac_islander_pop;  
	private double pct_pac_islander_pop;
	private int other_race_pop;
	private double pct_other_race_pop;
	private int hispanic_pop;  //MAJOR
	private double pct_hispanic_pop;
	
	// Education
	private int pop_18_24;
	private int pop_25;   //MAJOR
	private double pct_less_than_hs;
	private double pct_hs_grads;
	private double pct_some_col;
	private double pct_ass_degree;
	private double pct_bach_degre;
	private double pct_grad_degree;
	private double pct_hs_or_more;  //MAJOR
	private double pct_bach_or_more;
	private int median_earnings;    //MAJOR
	private int median_earnings_bach;   //MAJOR
	private int median_earnings_grad;
	
	// Housing
	private int num_households;   //MAJOR
	private int fam_households;   
	private double pct_households_fam;  //MAJOR
	private double avg_fam_size;
	private int num_fam_u18_child;
	private double pct_household_same_sex;
	private double pct_household_opp_sex;
	private double pct_one_unit_struct;
	private double pct_two_plus_unit_struct;
	private double pct_units_owner_occ;   //MAJOR
	private double pct_units_renter_occ;  //MAJOR 
	
	
	
	
	
	@Override
	public List<DbTableObject> getDelegateObjects() {
		// TODO Auto-generated method stub
		return null;
	}
	
}