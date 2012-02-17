#!/bin/bash
ps -u`whoami` -opid,comm,args | grep "org.jboss.Main" | awk '{ print $1 }' | xargs kill -9

ps -u`whoami` -opid,comm,args | grep "org.jboss.as.standalone" | awk '{ print $1 }' | xargs kill -9
