config_path=$(pwd)/config.properties
echo $config_path
sh ./sparrow-java-coder.sh -ct com.sparrow.chat.contact.po.Qun -config=$config_path
