import re
s = "/v2.1/os-aggregates/141/action"
p = re.compile("/os-aggregates/(.*?)[/action]{0,1}")
a = p.sub("/os-aggregates")
