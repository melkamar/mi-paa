vals = [(88, 20), (3, 2), (72, 36), (45, 55), (36, 12), (7, 92), (5, 11), (12, 80), (14, 66), (12, 90), (55, 43),
        (11, 17)]

capacity = 240


# best_result = -1

def solve_file(filename):
    with open(filename) as f:
        for line in f:
            parse_data_line(line)

def parse_data_line(text):
    data = text.split(" ")
    id = data[0]
    count = data[1]
    capacity = data[2]

    values = []
    for i in range(3, count - 3, 2):
        values.append((data[i], data[i + 1]))

    print("Parsed line:\nID:{}\nCount:{}\nCapacity:{}\nValues:{}\n".format(id, count, capacity, values))


def solve_bag_brute(bag_values, capacity):
    return rec_brute(bag_values, capacity, 0, 0)


def rec_brute(bag_values, capacity, cur_weight, cur_value):
    print("Recursing with list: {}".format(bag_values))

    best_res = 0
    for i in range(0, len(bag_values)):
        temp_weight = cur_weight + bag_values[i][1]

        if cur_weight > capacity:
            print("Overweight! Best: {}".format(cur_value))
            return cur_value
        else:
            temp_value = cur_value + bag_values[i][0]
            best_res = max(rec_brute(bag_values[i + 1:], capacity, temp_weight, temp_value), best_res)

    return best_res


# best = solve_bag_brute(vals, capacity)
# print("Best result: {}".format(best))

solve_file()