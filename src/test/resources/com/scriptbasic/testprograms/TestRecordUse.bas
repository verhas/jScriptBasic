R = new()
R.alma = 3
print R.alma
R.alma = new()
r.alma.korte = "haho"
print r.alma.korte
REM magically creates the sub records
r.z.k.l.t = 55
print r.z.k.l.t
REM can be used through 'get' and 'put'
print r.get("z").get("k").get("l").t
o = r.set("sss","sss")
o = r.set("xxx")
print r.sss