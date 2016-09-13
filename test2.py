import sys
import os
def detailtrace(info):
  retStr = ""
  curindex=0
  f = sys._getframe()
  f = f.f_back    # first frame is detailtrace, ignore it
  while hasattr(f, "f_code"):
    co = f.f_code
    retStr = "%s(%s:%s)->"%(os.path.basename(co.co_filename),
         co.co_name,
         f.f_lineno) + retStr
    f = f.f_back
  print retStr+info



def foo():
  detailtrace("hello world")
def bar():
  foo()
def main():
  bar()
if __name__ == "__main__":
  main()
