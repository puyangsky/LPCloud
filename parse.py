import re

LOGPATH = "/home/pyt/log/test.log"

server_id_pattern = re.compile("/servers/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
server_groups_pattern = re.compile("/os-server-groups/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
flavor_id_pattern = re.compile("/flavors/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
flavor_extra_specs_pattern = re.compile("/flavors/{flavor_id}/os-extra_specs/.*")
hypervisors_id_pattern = re.compile("/os-hypervisors/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
keypair_name_pattern = re.compile("/os-keypairs/.*")
agents_build_id_pattern = re.compile("/os-agents/.*")
aggregates_id_pattern = re.compile("/os-aggregates/[-]?\d*")


id_pattern = re.compile("[0-9a-zA-Z]{32}")


for line in open(LOGPATH, "a+"):
	if not line.startswith("##"):
#		print line
		line_items = line.split("\t")
		if len(line_items) == 5:
			request = line_items[2].strip("'")
			request_items = request.split(" ")
			if len(request_items) == 3:
				method = request_items[0]
				url = request_items[1]
				#print url
				url = server_id_pattern.sub("/servers/{server_id}", url)
				url = server_groups_pattern.sub("/os-server-groups/{server_group_id}", url)
				url = flavor_id_pattern.sub("/flavors/{flavor_id}", url)

				url = hypervisors_id_pattern.sub("/os-hypervisors/{hypervisors_id}", url)
				url = flavor_extra_specs_pattern.sub("/flavors/{flavor_id}/os-extra_specs/{flavor_extra_spec_key}", url)
				url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)

				url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)
				url = agents_build_id_pattern.sub("/os-agents/{agent_build_id}", url)
				url = aggregates_id_pattern.sub("/os-aggregates/{aggregate_id}", url)
				url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)
				url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)

				url = id_pattern.sub("%ID%", url)
				print method, "\t", url
				with open("/home/pyt/Desktop/python/res.txt", "a+") as f:
					f.write("%s\t%s\n" % (method, url))












