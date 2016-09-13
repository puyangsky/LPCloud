import re

LOGPATH = "/home/pyt/log/test.log"
RES = "/home/pyt/Desktop/python/res.txt"

with open(RES, "w") as f:
	f.truncate()

server_id_pattern = re.compile("/servers/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
server_groups_pattern = re.compile("/os-server-groups/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
flavor_id_pattern = re.compile("/flavors/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}|\d+)")
flavor_extra_specs_pattern = re.compile("/flavors/{flavor_id}/os-extra_specs/.*")
hypervisors_id_pattern = re.compile("/os-hypervisors/.*?/")
hypervisors_id_pattern2 = re.compile("/os-hypervisors/(\d+|[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})")
keypair_name_pattern = re.compile("/os-keypairs/.*")
agents_build_id_pattern = re.compile("/os-agents/.*")
aggregates_id_pattern = re.compile("/os-aggregates/[-]?\d*")
hosts_pattern = re.compile("/os-hosts/.*/")
hosts_pattern2 = re.compile("^/os-hosts/(.*)(?!/(reboot|shutdown|startup))$")
audit_log_pattern = re.compile("/os-instance_usage_audit_log/.*")

quota_sets_pattern = re.compile("/os-quota-sets/[0-9a-f]{32}")
tenant_usage_pattern = re.compile("/os-simple-tenant-usage/[0-9a-f]{32}")

security_group_pattern = re.compile("/os-security-group-default-rules/.*")
networks_pattern = re.compile("/os-networks/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")

fixed_ips_pattern = re.compile("/os-fixed-ips/(\d{1,4}\.\d{1,4}\.\d{1,4}\.\d{1,4}|my\.invalid\.ip)")

for line in open(LOGPATH, "r"):
	if not line.startswith("##"):
#		print line
		line_items = line.split("\t")
		if len(line_items) == 5:
			request = line_items[2].strip("'")
			request_items = request.split(" ")
			if len(request_items) == 3:
				method = request_items[0]
				url = request_items[1]
				if url.find("?") != -1:
					url = url.split("?")[0]
				if url.find("/v2.1") != -1:
					url = url.split("/v2.1")[-1]
				#print url
				url = server_id_pattern.sub("/servers/{server_id}", url)
				url = server_groups_pattern.sub("/os-server-groups/{server_group_id}", url)
				url = flavor_id_pattern.sub("/flavors/{flavor_id}", url)

				url = hypervisors_id_pattern.sub("/os-hypervisors/{hypervisors_id}/", url)
				url = hypervisors_id_pattern2.sub("/os-hypervisors/{hypervisors_id}", url)
				url = flavor_extra_specs_pattern.sub("/flavors/{flavor_id}/os-extra_specs/{flavor_extra_spec_key}", url)
				url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)

				url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)
				url = agents_build_id_pattern.sub("/os-agents/{agent_build_id}", url)
				url = aggregates_id_pattern.sub("/os-aggregates/{aggregate_id}", url)
				
				url = hosts_pattern2.sub("/os-hosts/{host_name}", url)
				url = hosts_pattern.sub("/os-hosts/{host_name}/", url)
				
				url = audit_log_pattern.sub("/os-instance_usage_audit_log/{before_timestamp}", url)
				

				url = quota_sets_pattern.sub("/os-quota-sets/{tenant_id}", url)
				url = tenant_usage_pattern.sub("/os-simple-tenant-usage/{tenant_id}", url)
				
				url = security_group_pattern.sub("/os-security-group-default-rules/{security_group_default_rule_id}", url)
				url = networks_pattern.sub("/os-networks/{network_id}", url)
				url = fixed_ips_pattern.sub("/os-fixed-ips/{fixed_ip}", url)

				#url = id_pattern.sub("%ID%", url)
				print method, "\t", url
				with open(RES, "a+") as f:
					f.write("%s\t%s\n" % (method, url))












