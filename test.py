from oslo_log import log as logging
#print __name__
LOG = logging.getLogger("test")

def parse_diff():
	f1 = open("./func_name.txt", "r")
	#print f1.read()
	names = f1.read().split("\n")
	#print names
	f2 = open("./func_res.txt", "r")
	names1 = f2.read().split("\n")
	#print names1
	index = 0
	for i in names:
		if i != "" and i not in names1:
			print i
			index += 1
	print("\n\rtotal diff: %d\n" % index)


def test():
	LOG.debug("fucking log")

test()
LOG.info("Python Standard Logging")
