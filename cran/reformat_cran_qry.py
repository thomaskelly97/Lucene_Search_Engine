# A python script to reformat the cranqrel file in the required format for trec_eval
f = open("./cran.qry", "r")
write_file = open("reformatted_cran.qry", "a")

line = "-"
correct_no = 1
while line != "" or line == None:
    line = f.readline()
    split_line = line.split(" ")

    if split_line[0] == ".I":
        abstract_num = int(split_line[1].strip())
        split_line[1] = str(correct_no)
        split_line.insert(2, "\n")
        print(split_line)
        formatted_line = " ".join(split_line)
        correct_no = correct_no + 1
        print(formatted_line)
        write_file.write(formatted_line)
    else:
        print(line)
        write_file.write(line)


print("Finished")
