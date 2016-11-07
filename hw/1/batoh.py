import os
import time


def solve_file(filename):
    """
    Solves all instances in a file both by bruteforce and with heuristics.
    :param filename:
    :return: Dictionary of: "instance-id":"bruteforce-value". Useful for checking if result correct (it should always be)
    """
    ret_dict = {}
    with open(filename) as f:
        total_rel_err = 0
        total_brute_duration = 0
        total_heuri_duration = 0
        instance_count = 0
        max_rel_err = -1
        values_count = -1

        for line in f:
            instance_count += 1
            # For each line time bruteforce and then heuristics.
            parsed_entry = parse_data_line(line)

            if values_count == -1:
                values_count = len(parsed_entry[3])
            elif values_count != len(parsed_entry[3]):
                raise ValueError(
                    "Values count are not equal! Had:{}  now found: {}".format(values_count, len(parsed_entry[3])))

            brute_start = time.process_time()
            # if values_count < 10:
            #     for i in range(100):
            #         brute_res = solve_bag_brute(parsed_entry[3], parsed_entry[2])
            #     brute_end = time.process_time()
            #     brute_duration = (brute_end - brute_start) / 100.0
            # else:
            brute_res = solve_bag_brute(parsed_entry[3], parsed_entry[2])
            brute_end = time.process_time()
            brute_duration = brute_end - brute_start

            heuristic_start = time.process_time()
            for i in range(100):
                heuristic_res = solve_bag_heuristic(parsed_entry[3], parsed_entry[2])
            heuristic_end = time.process_time()
            heuristic_duration = (heuristic_end - heuristic_start) / 100.0

            total_brute_duration += brute_duration
            total_heuri_duration += heuristic_duration

            # print("heuri : {}".format(heuristic_res))
            # print("{}".format(brute_res))

            rel_err = (brute_res - heuristic_res) / float(brute_res)
            total_rel_err += rel_err
            max_rel_err = max(max_rel_err, rel_err)

            ret_dict[parsed_entry[0]] = brute_res

        avgrelerr = total_rel_err / float(instance_count)
        avgbrutetime = total_brute_duration / float(instance_count)
        avgheuritime = total_heuri_duration / float(instance_count)
        print("n={:2d} avgrelerr={} maxrelerr={} brutetime={:f} heuritime={:f}".format(values_count, avgrelerr,
                                                                                       max_rel_err,
                                                                                       avgbrutetime, avgheuritime))
    return ret_dict


def parse_data_line(text):
    """
    Parses an instance from a source file.
    :param text: The line to parse.
    :return: Tuple of (ID, size, bag_capacity, [(weight1,val1), (weight2,val2), ...])
    """
    data = text.split(" ")
    id = int(data[0])
    count = int(data[1])
    capacity = int(data[2])

    values = []
    for i in range(3, len(data), 2):
        values.append((int(data[i]), int(data[i + 1])))

    return id, count, capacity, values


def solve_bag_heuristic(bag_values, capacity):
    heuristic_values = [(item[1] / float(item[0]), item) for item in bag_values]
    sorted_heuristic_values = sorted(heuristic_values, key=lambda tup: tup[0], reverse=True)

    total_weight = 0
    total_cost = 0
    for item in sorted_heuristic_values:
        if total_weight + item[1][0] <= capacity:
            total_weight += item[1][0]
            total_cost += item[1][1]

    return total_cost


brute_count = 0


def solve_bag_brute(bag_values, capacity):
    # print("Solve_brute")
    return rec_brute(bag_values, capacity, 0, 0, 0)


def rec_brute(bag_values, capacity, cur_weight, cur_value, start_index):
    best_res = 0

    if start_index == len(bag_values):
        return cur_value

    for i in range(start_index, len(bag_values)):
        temp_weight = cur_weight + bag_values[i][0]

        if temp_weight > capacity:
            best_res = max(cur_value, best_res)
        else:
            temp_value = cur_value + bag_values[i][1]
            best_res = max(
                rec_brute(bag_values, capacity, temp_weight, temp_value, start_index=i + 1), best_res)

    return best_res


# def rec_brute(bag_values, capacity, cur_weight, cur_value, start_index, vector, indent=0):
#     best_res = 0
#
#     if start_index == len(bag_values):
#         return cur_value
#
#     for i in range(start_index, len(bag_values)):
#         temp_weight = cur_weight + bag_values[i][0]
#
#         if temp_weight > capacity:
#             best_res = max(cur_value, best_res)
#         else:
#             temp_value = cur_value + bag_values[i][1]
#             newvect = list(vector)
#             newvect[i] = 1
#             best_res = max(
#                 rec_brute(bag_values, capacity, temp_weight, temp_value, start_index=i + 1, vector=newvect,
#                           indent=(indent + 1)),
#                 best_res)
#
#     return best_res


def check_solutions(solved_dict, fn):
    new_fn = fn.replace(".inst.dat", ".sol.dat")
    new_fn = os.path.join("data", new_fn)

    solution_dict = {}

    with open(new_fn) as f:
        for line in f:
            fields = line.split(" ")
            solution_dict[int(fields[0])] = int(fields[2])

    all_ok = True
    for key in solved_dict.keys():
        if solution_dict[key] != solved_dict[key]:
            print("NOT EQUAL! ID:{}      yours:{}  correct:{}".format(key, solved_dict[key], solution_dict[key]))
            all_ok = False

    if all_ok:
        pass
    else:
        print("There was an error during calculation! Results mismatch.")


files = ['knap_4.inst.dat', 'knap_10.inst.dat', 'knap_15.inst.dat', 'knap_20.inst.dat', 'knap_22.inst.dat',
         'knap_25.inst.dat', 'knap_27.inst.dat', 'knap_30.inst.dat', 'knap_32.inst.dat', 'knap_35.inst.dat',
         'knap_37.inst.dat', 'knap_40.inst.dat']

# files = ['knap_15.inst.dat']

for txt in files:
    solve_dict = solve_file(os.path.join("data", txt))
    check_solutions(solve_dict, txt)
