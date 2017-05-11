import re
import sys

LOGPATH = "/home/pyt/log/test.log"
RES = "/home/pyt/Desktop/python/res.txt"
API_MAP = "/home/pyt/Desktop/python/api.txt"
RES_ARRAY = "/home/pyt/Desktop/python/array.txt"

THRESHOLD = 1

api_map = {}

server_id_pattern = re.compile("/servers/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}|\d+)")
server_groups_pattern = re.compile("/os-server-groups/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
flavor_id_pattern = re.compile("/flavors/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}|\d+)")
flavor_extra_specs_pattern = re.compile("/flavors/\d*/os-extra_specs/.*")
hypervisors_id_pattern = re.compile("/os-hypervisors/.*?/")
hypervisors_id_pattern2 = re.compile(
    "/os-hypervisors/(\d+|[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})")
keypair_name_pattern = re.compile("/os-keypairs/.*")
agents_build_id_pattern = re.compile("/os-agents/.*")
aggregates_id_pattern = re.compile("/os-aggregates/[-]?\d*")
hosts_pattern = re.compile("/os-hosts/[a-zA-Z0-9-_]*/")
hosts_pattern2 = re.compile("/os-hosts/(tempest-rand_hostname-[\d]*|pyt)")
audit_log_pattern = re.compile("/os-instance_usage_audit_log/.*")

quota_sets_pattern = re.compile("/os-quota-sets/[0-9a-f]{32}")
tenant_usage_pattern = re.compile("/os-simple-tenant-usage/[0-9a-f]{32}")

os_security_groups_pattern = re.compile("/os-security-groups/.*")

security_group_pattern = re.compile("/os-security-group-default-rules/(.*)")
networks_pattern = re.compile("/os-networks/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")

fixed_ips_pattern = re.compile("/os-fixed-ips/(\d{1,4}\.\d{1,4}\.\d{1,4}\.\d{1,4}|my\.invalid\.ip)")


def clear_file():
    with open(RES, "w") as f:
        f.truncate()


def parse_source_file():
    # clear file before write
    clear_file()
    for line in open(LOGPATH, "r"):
        if line.startswith("##"):
            # line = line.strip("##")
            line = line[2:-3]
            line_items = line.split(" ")
            if (len(line_items) == 3):
                # print line_items[2]
                with open(RES, "a+") as f:
                    f.write("\n%s\n" % line_items[2])

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
                    # print url
                    url = server_id_pattern.sub("/servers/{server_id}", url)
                    url = server_groups_pattern.sub("/os-server-groups/{server_group_id}", url)
                    url = flavor_id_pattern.sub("/flavors/{flavor_id}", url)

                    url = hypervisors_id_pattern.sub("/os-hypervisors/{hypervisors_id}/", url)
                    url = hypervisors_id_pattern2.sub("/os-hypervisors/{hypervisors_id}", url)
                    url = flavor_extra_specs_pattern.sub("/flavors/{flavor_id}/os-extra_specs/{flavor_extra_spec_key}",
                                                         url)
                    url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)

                    url = keypair_name_pattern.sub("/os-keypairs/{keypair_name}", url)
                    url = agents_build_id_pattern.sub("/os-agents/{agent_build_id}", url)
                    url = aggregates_id_pattern.sub("/os-aggregates/{aggregate_id}", url)

                    # url = hosts_pattern2.sub("/os-hosts/{host_name}", url)
                    url = hosts_pattern.sub("/os-hosts/{host_name}/", url)
                    url = hosts_pattern2.sub("/os-hosts/{host_name}", url)

                    url = audit_log_pattern.sub("/os-instance_usage_audit_log/{before_timestamp}", url)

                    url = quota_sets_pattern.sub("/os-quota-sets/{tenant_id}", url)
                    url = tenant_usage_pattern.sub("/os-simple-tenant-usage/{tenant_id}", url)

                    url = security_group_pattern.sub(
                        "/os-security-group-default-rules/{security_group_default_rule_id}", url)
                    url = networks_pattern.sub("/os-networks/{network_id}", url)
                    url = fixed_ips_pattern.sub("/os-fixed-ips/{fixed_ip}", url)

                    url = os_security_groups_pattern.sub("/os-security-groups/{security_group_id}", url)
                    # url = id_pattern.sub("%ID%", url)
                    # print method, "\t", url
                    with open(RES, "a+") as f:
                        f.write("\t%s-%s\n" % (method, url))


# api<==>index map
def fill_api_map():
    with open(API_MAP, "w") as f:
        f.truncate()
    unique_api = []
    for line in open(RES, "r"):
        if line.startswith("\t"):
            line = line.strip("\t").strip("\n").strip("/")

            if line not in unique_api:
                # print line
                unique_api.append(line)
    index = 0
    for item in unique_api:
        # print index, "\t", item
        api_map[item] = index
        with open(API_MAP, "a+") as f:
            f.write("%s\n" % (item))
        index += 1


'''
	for item in api_map.keys():
		if api_map[item] == 2:
			api_map.pop(item)
'''


def parse_dependencies():
    # fill the api_map before parsing dependencise
    fill_api_map()

    # initialize the result array with 0, if i==j, result_list[i][j] = 0
    result_list = [[0 for col in range(len(api_map))] for row in range(len(api_map))]
    # for i in range(len(api_map)):
    #	result_list[i][i] = 1

    content = open(RES, "r").read()
    # print content
    items = content.split("\n\n")
    unique_list = []
    # remove duplicate items
    for i in items:
        if i not in unique_list:
            unique_list.append(i)

    if len(unique_list) > 0:
        for item in unique_list:
            # print item
            lines = item.split("\n")
            # if len(lines) <= 1, ignore it
            if len(lines) > 2:
                # remove first line like "test_get_usage_tenant"
                innner_list = lines[1:]
                unique_inner_list = []
                for innner_list_item in innner_list:
                    # purify the inner api item
                    if innner_list_item.startswith("\t"):
                        innner_list_item = innner_list_item.strip("\t").strip("\n").strip("/")
                        if innner_list_item not in unique_inner_list and i != "":
                            # remove "\t" before the line
                            unique_inner_list.append(innner_list_item)
                # ignore when there is only one item in list
                l = len(unique_inner_list)
                if l > 1:
                    # print unique_inner_list
                    for j in range(l - 1):
                        for k in range(j + 1, l):
                            result_list[api_map[unique_inner_list[k]]][api_map[unique_inner_list[j]]] += 1
                            result_list[api_map[unique_inner_list[j]]][api_map[unique_inner_list[k]]] += 1

    # record to file
    '''
	with open(RES_ARRAY, "w") as f:
		f.truncate()
	for row in result_list:
		print row
		with open(RES_ARRAY, "a+") as f:
			f.write("%r\n" % row)
	'''
    return result_list


# partition api by dfs					
def partition_api_by_dfs():
    part_count = 0
    result_list = parse_dependencies()
    length = len(result_list)
    if length > 0:
        for i in range(length):
            for j in range(length):
                if result_list[i][j] != 0:
                    dfs(result_list, i, j)
                    part_count += 1

                    # print "total partition count : ", part_count/2


def dfs(result_list, row, col):
    if row < 0 or row >= len(result_list) or col < 0 or col >= len(result_list[0]) or result_list[row][col] == 0:
        return
    result_list[row][col] = 0
    dfs(result_list, row - 1, col)
    dfs(result_list, row + 1, col)
    dfs(result_list, row, col - 1)
    dfs(result_list, row, col + 1)


# partition api by bfs
def partition_api_by_bfs():
    result_list = parse_dependencies()
    num = len(result_list)
    # store the partition result
    partition_res_list = [[] for index in range(num)]

    # mark the api if visited
    visited = [False for i in range(num)]
    # mark the head of one api
    head = {}
    for index in range(num):
        head[index] = index

    for i in range(num):
        for j in range(i + 1, num):
            if result_list[i][j] > THRESHOLD:
                if visited[i] == False:
                    if visited[j] == False:
                        head[j] = head[i]
                        visited[i] = visited[j] = True
                        partition_res_list[head[i]].append(j)
                    if visited[j] == True:
                        head[i] = head[j]
                        visited[i] = True
                        partition_res_list[head[j]].append(i)
                if visited[i] == True:
                    if visited[j] == False:
                        head[j] = head[i]
                        visited[j] = True
                        partition_res_list[head[i]].append(j)
                    if visited[j] == True:
                        # if head[i] == head[j], do nothing

                        # if head[i] != head[j], merge two
                        if head[i] != head[j]:
                            partition_res_list[head[i]].extend(partition_res_list[head[j]])
                            del partition_res_list[head[j]]
                            head[j] = head[i]

    # print the partition result
    count = 0
    for item in partition_res_list:
        if len(item) > 0:
            count += 1
            print(item)
    print(count)


def parse_union():
    fill_api_map()
    # func_map = fill_func_map()
    content = open(RES, "r").read()
    # print content
    items = content.split("\n\n")
    unique_list = []
    res_list = []

    # remove duplicate items
    for i in items:
        if i not in unique_list:
            unique_list.append(i)

    if len(unique_list) > 0:
        for item in unique_list:
            # print item
            lines = item.split("\n")
            # if len(lines) <= 1, ignore it
            if len(lines) > 0:
                func_name = lines[0]
                if func_name.startswith("test"):
                    innner_list = lines[1:]
                    unique_inner_list = []
                    # use index replacing api name
                    unique_index_list = []
                    for innner_list_item in innner_list:
                        # purify the inner api item
                        if innner_list_item.startswith("\t"):
                            innner_list_item = innner_list_item.strip("\t").strip("\n").strip("/")
                            if innner_list_item not in unique_inner_list and i != "":
                                # remove "\t" before the line
                                unique_inner_list.append(innner_list_item)
                                unique_index_list.append(api_map[innner_list_item])
                    unique_index_list = sorted(unique_index_list)
                    # unique_index_list.insert(0, func_map[func_name])
                    unique_index_list.insert(0, func_name)
                    if unique_index_list not in res_list:
                        res_list.append(unique_index_list)
    # for i in res_list:
    #	print i
    return res_list


def fill_func_map():
    content = open(RES, "r").read()
    # print content
    items = content.split("\n\n")
    func_list = []
    func_map = {}
    for item in items:
        inner = item.split("\n")
        if len(inner) < 2:
            continue
        item = inner[0]
        if item not in func_list and item.startswith("test"):
            func_list.append(item)
    index = 0
    with open("/home/pyt/Desktop/python/func_name.txt", "w") as f:
        f.truncate()
    for item in func_list:
        func_map[item] = index
        with open("/home/pyt/Desktop/python/func_name.txt", "a+") as f:
            f.write("%s\n" % item)
        index += 1
    print("number of func : %d\n" % len(func_map))
    return func_map


def get_intersection():
    # head = {}
    threshold = 0
    fname = "/home/pyt/Desktop/python/func_res.txt"
    with open(fname, "w") as f:
        f.truncate()
    for threshold in range(7):
        res_list = parse_union()
        # for i in res_list:
        #	print i[0]
        # print len(res_list)
        length = len(res_list)
        # visited = [False for i in range(length)]

        func_list = [[] for row in range(length)]

        # init every node
        # for i in range(length):
        #	head[res_list[i][0]] = i
        '''
		for i in range(length-1):
			for j in range(i+1, length):
				intersection = list(set(res_list[i]).intersection(set(res_list[j])))
				# THRESHOLD shows the relationship of two function
				if len(intersection) > THRESHOLD:
					print("%s\t%s\t%r" %(res_list[i][0], res_list[j][0], intersection))
					# merge i and j
					res_list[i] = list(set(res_list[i]).union(set(res_list[j][1:])))
					res_list[j] = []
				
					if visited[i] == False:
						if visited[j] == False:
							index = head[res_list[i][0]]
							head[res_list[j][0]] = index
							func_list[index].append(res_list[i][0])
							func_list[index].append(res_list[j][0])
							visited[i] = visited[j] = True
						elif visited[j] == True:
							index = head[res_list[j][0]]
							head[res_list[i][0]] = index
							func_list[index].append(res_list[i][0])
							visited[i] = True
					elif visited[i] == True:
						if visited[j] == False:
							index = head[res_list[i][0]]
							head[res_list[j][0]] = index
							func_list[index].append(res_list[j][0])
							visited[j] = True
						elif visited[j] == True:
							index_i = head[res_list[i][0]]
							index_j = head[res_list[j][0]]
							func_list[index_i] = list(set(func_list[index_i]).union(set(func_list[index_j])))
							# clear the index_j
							#func_list[index_j] = []
							head[res_list[j][0]] = index_i
					'''
        for i in range(length - 1):
            if len(res_list[i]) < 1:
                continue
            flag = 1
            while (flag > 0):
                for j in range(i + 1, length):
                    if len(res_list[i]) > 0 and len(res_list[j]) > 0:
                        intersection = list(set(res_list[i]).intersection(set(res_list[j])))
                        # if res_list[j][0] == "test_flavor_get_nonexistent_key":
                        #	print res_list[j], "\t", res_list[i], "\t", intersection
                        if len(intersection) > threshold:
                            # print "i: ", res_list[i]
                            # print "j: ", res_list[j]
                            flag += 1
                            # merge j into i
                            for item in res_list[j][1:]:
                                if item not in res_list[i]:
                                    res_list[i].append(item)
                            func_list[i].append(res_list[i][0])
                            func_list[i].append(res_list[j][0])
                            res_list[j] = []
                flag -= 1
                if flag == 0:
                    print("The %dth break..." % i)

        with open(fname, "a+") as f:
            f.write("\n========================================================\n")
            f.write("Threshold: %d\n" % threshold)

        unique_func_list = []
        # remove duplicate and blank item in func_list
        for item in func_list:
            item = list(set(item))
            if len(item) > 0 and item not in unique_func_list:
                unique_func_list.append(item)
                with open(fname, "a+") as f:
                    for i in item:
                        f.write("%s\n" % i)
                    f.write("\n")
        with open(fname, "a+") as f:
            f.write("total partition count: %d\n" % len(unique_func_list))
            # print ("total partition count: %d\n" % len(unique_func_list))

            # test.parse_diff()


# parse_source_file()
# parse_dependencies()
# fill_api_map()
if __name__ == '__main__':
    if sys.argv[1] == '0':
        parse_source_file()
    if sys.argv[1] == '1':
        fill_api_map()
    # if sys.argv[1] == '2':
    # 	partition_api()
    if sys.argv[1] == '3':
        partition_api_by_bfs()
    if sys.argv[1] == '4':
        # if len(sys.argv) < 3:
        #	print "You must input second argument!"
        # else:
        get_intersection()
    if sys.argv[1] == '5':
        fill_func_map()
