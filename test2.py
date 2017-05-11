import sys
import os
def detailtrace():
  retStr = ""
  curindex=0
  f = sys._getframe()
  f = f.f_back    # first frame is detailtrace, ignore it
  while hasattr(f, "f_code"):
    co = f.f_code
    retStr = "%s(%s:%s)\n"%(os.path.abspath(co.co_filename),
         co.co_name,
         f.f_lineno) + retStr
    f = f.f_back
  with open("/opt/stack/stack.log", "a+") as f1:
	f1.write("%s\n" % retStr)



def foo():
  detailtrace("hello world")
def bar():
  foo()
def main():
  bar()
if __name__ == "__main__":
  main()
