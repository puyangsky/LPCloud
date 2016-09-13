def show():
	name = "pyt"
	age = 18
	def inner(a):
		print "name:", name, "\nage", age, "\nparam:", a
	return inner

method = show()
method("test")
