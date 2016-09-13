def deco(func):
    def _deco():
        print("before myfunc() called.")
        #print("  after myfunc() called.")
        return func()
    return _deco
 
@deco
def myfunc():
    print(" myfunc() called.")
    return 'ok'
 
a = myfunc()

print a
#myfunc()
