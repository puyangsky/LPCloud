from __future__ import print_function
import sys
import os

__author__ = 'Administrator'


def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)


BASE_DIR = os.path.dirname(os.path.abspath(__file__))
SOURCE_DIR = os.path.join(BASE_DIR, "source_file")

SERVICES = [
    "nova",
    "glance",
    "keystone",
    "cinder"
]

nova_file_path = os.path.join(SOURCE_DIR, "stack.log.nova.new1")
if os.path.exists(nova_file_path):
    print(nova_file_path)
    nova = open(nova_file_path, "r").readline()
    print(nova)
else:
    eprint("file not exists")