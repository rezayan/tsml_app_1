package ir.topcoders.pol.utils.persian_tools;

public class DateFields
{

	private int	year;

	public void setYear(int year)
	{
		this.year = year;
	}

	public int getYear()
	{
		return year;
	}

	private int	month;

	public void setMonth(int month)
	{
		this.month = month;
	}

	public int getMonth()
	{
		return month;
	}

	private int	day;

	public void setDay(int day)
	{
		this.day = day;
	}

	public int getDay()
	{
		return day;
	}

	public DateFields(int year, int month, int day)
	{
		super();
		setYear(year);
		setMonth(month);
		setDay(day);
	}

	public DateFields()
	{
		this(0, 0, 0);
	}

	public String toString()
	{
		return "" + year + "/" + (month + 1) + "/" + day;
	}
}
