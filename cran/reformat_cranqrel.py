# A python script to reformat the cranqrel file in the required format for trec_eval
f = open("./cranqrel", "r")
write_file = open("reformatted_cranqrel", "a")

line = f.readline()
while line != "" or line == None:
    line = f.readline()
    split_line = line.split(" ")
    if len(split_line) == 1:
        break
    split_line.insert(1, "0")
    formatted_line = " ".join(split_line)
    print(formatted_line)
    write_file.write(formatted_line)

print("Finished")
