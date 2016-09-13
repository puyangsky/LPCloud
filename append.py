import os
import os.path

path = "/opt/stack/tempest/tempest/api/compute/admin/"
#path = "/home/pyt/Desktop/test/"

string = '''\nwith open("/home/pyt/log/test.log", "a+") as f:
    f.write("$$Begin test '''

string1 = r'''$$\n")'''


def getFilePath(path):
	filePathList = []
	for a, b, c in os.walk(path):
		#print a
		for i in c:
			if i.endswith("py"):
				#print i
				path = os.path.join(a, i)
				#print path
				filePathList.append(path)
	return filePathList

def addHook(filePathList):
	if filePathList == None:
		return
	for path in filePathList:
		print path
		with open(path, "a+") as f:
			f.write(string + path + string1)


filePath = getFilePath(path)
addHook(filePath)
