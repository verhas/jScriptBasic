' Test functions with numeric parameter
assert("year_num", year(0)=1899)
assert("moth_num", month(0)=12)
assert("day_num", day(0)=30)

' Test functions with string parameter
d="2020-02-24"
assert("year_parse_string", year(d)=2020)
assert("month_parse_string", month(d)=2)
assert("day_parse_string", day(d)=24)

' Test functions with date parameter
ds=DateSerial(year(d),month(d),day(d))
assert("year_from_date", year(ds)=2020)
assert("month_from_date", month(ds)=2)
assert("day_from_date", day(ds)=24)

' Test isdate
assert("isdate_date", isdate(ds))
assert("isdate_date2", not isdate(""))
assert("isdate_date3", isdate( date() ) )

' Test conversion functions
dt = CDate(d)
assert("cdate_isdate", IsDate(dt))
assert("cdate_year", year(dt)=2020)
assert("cdate_month", month(dt)=2)
assert("cdate_day", day(dt)=24)

' Test date aritmetics
ds2=DateSerial(year(d),month(d),day(d))+1
assert("inc_date", day(ds2)=25 )
ds3=ds2-1
assert("dec_date", day(ds3)=24 )

' Compare dates
assert("eq", ds=ds3 )
assert("neq", ds<>ds2 )
assert("lt", ds<ds2 )
assert("gt", ds2>ds3 )

' Compare date with numerical
assert("date_with_num", ds>0)
assert("date_with_num2", ds<(200*365))

' Test dateadd
assert("dateadd", dateadd("d", 1, ds)=ds2)
assert("dateadd", dateadd("d", -1, ds2)=ds)
assert("dateadd", dateadd("ww", 1, ds)=CDate("2020-03-02"))
assert("dateadd", dateadd("m", 2, ds)=CDate("2020-04-24"))
assert("dateadd", dateadd("yyyy", 3, ds)=CDate("2023-02-24"))

' Test datediff
assert("datediff", datediff("d", ds, ds2)=1)
assert("datediff", datediff("d", ds2, ds)=-1)

print "DONE"
