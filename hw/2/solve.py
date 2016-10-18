import time
import numpy
import os
import bbsolver
import dpsolver

def solve_knapsack_by_weight(data):
    """
    :param data: List of tuples: (value, weight)
    :return:
    """


def parse_data_line(text):
    """
    Parses an instance from a source file.
    :param text: The line to parse.
    :return: Tuple of (ID, size, bag_capacity, [(val1, weight1), (val2, weight2), ...])
    """
    data = text.split(" ")
    id = int(data[0])
    count = int(data[1])
    capacity = int(data[2])

    values = []
    for i in range(3, len(data), 2):
        values.append((int(data[i + 1]), int(data[i])))

    return id, count, capacity, values


files = ['knap_40.inst.dat']

# (27 38) (2 86) (41 112) (1 0) (25 66) (1 97) (34 195) (3 85) (50 42) (12 223)

for fn in [os.path.join('data', fn) for fn in files]:
    with open(fn) as file:
        time_file = 0
        for line in file:
            data = parse_data_line(line)

            # bbsolve
            # solver = bbsolver.BBSolver(data[3], data[2])
            #
            # time_start = time.process_time()
            # res = solver.solve_bag_brute()
            # time_end = time.process_time()
            # time_file += time_end - time_start
            #
            # print("{}".format(res))


            # dpsolve
            solver = dpsolver.DPSolver(data[3], data[2])
            res = solver.solve_bag()
            print(res)

            # break


        print("Time: {}".format(time_file/50.0))
