#!/bin/bash
kill -9 `ps -ef|grep re_build|grep -v grep|awk '{print $2}'`
