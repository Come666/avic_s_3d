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
from shutil import copyfile
type = sys.getfilesystemencoding()
reload(sys)
sys.setdefaultencoding('utf8')


def getDirList( p ):
    p = str( p )
    
    if p=="":
          return [ ]
    if 1==2:
        p = p.replace( "/","\\")
        if p[ -1] != "\\":
             p = p+"\\"

    a = os.listdir( p )
   
    return a


#path = sys.argv[1]
path = "jar:file:/Users/gouyunfei/Library/Application%20Support/eTeks/Sweet%20Home%203D/Furniture-3-3233081815531655544.pref!/textured_mesh.obj"
tmp_path = path[9:]
pos = tmp_path.find("!")
file_path = tmp_path[:pos]
obj_path = tmp_path[pos:]
if obj_path[0]=="!":
    obj_path=obj_path[1:]
jar_file = file_path.split("/")[-1]

#print jar_file


file_path = file_path.replace("%20", " ")
#print file_path
# extract file
#os.system("cp -fr \"%s\"  \"%s.bak\""%(file_path, file_path))
#os.system("cp -fr \"%s\"  ."%(file_path))
#os.system("jar xf %s"%(jar_file))

copyfile("%s"%(file_path),  "%s.bak"%(file_path))
copyfile("%s"%(file_path),  "%s/%s"%(os.getcwd(), jar_file))
os.system("jar xf %s"%(jar_file))

files = os.listdir(".")

tmp_files = []
for f in files:
    if f==jar_file:
        continue
    if f=="smooth.py":
        continue
    if f=="tmp_%s.bak"%(jar_file):
        continue
    tmp_files.append(f)

for f in tmp_files:
    print f

# package
#print "jar cvf tmp_%s.bak %s"%(jar_file, " ".join(tmp_files))
os.system("jar cf tmp_%s.bak %s"%(jar_file, " ".join(tmp_files)))
#os.system("rm %s"%(" ".join(tmp_files)))
#os.system("mv %s '%s'"%("tmp_%s.bak"%(jar_file), file_path))
shutil.move("tmp_%s.bak"%(jar_file), file_path)


#os.system("cp \"%s\"  \"%s\""%(file_path, file_path))
#os.system("cp \"%s\"  \"%s.bak\""%(file_path, file_path))
#os.system("cp \"%s\"  \"%s.bak\""%(file_path, file_path))
