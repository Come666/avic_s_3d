#coding:utf8
import MySQLdb
import sys
import json
import datetime
import traceback
import time
import re
import os
import sys

type = sys.getfilesystemencoding()
reload(sys)
sys.setdefaultencoding('utf8')

#path = sys.argv[1]
path = "jar:file:/Users/gouyunfei/Library/Application%20Support/eTeks/Sweet%20Home%203D/Furniture-3-3233081815531655544.pref!/textured_mesh.obj"
tmp_path = path[9:]
pos = tmp_path.find("!")
file_path = tmp_path[:pos]
obj_path = tmp_path[pos:]
if obj_path[0]=="!":
    obj_path=obj_path[1:]
print obj_path