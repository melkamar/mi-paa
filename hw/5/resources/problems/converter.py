files_count = 99

import random
for i in range(1, files_count+1):
    with open(r'd:\cvut-checkouted\mi-paa\hw\5\resources\problems\set\uf75-0{}.cnf'.format(i)) as f:
        lines = f.readlines()
        lines = lines[:153]
        lines[7] = 'p cnf 75  145\n'

        lines.append(" ".join([str(random.randint(1, 100)) for i in range(0, 75)])+" 0")
        print(lines)

    with open(r'd:\cvut-checkouted\mi-paa\hw\5\resources\problems\set\easy\uf75-0{}.cnf'.format(i), 'w') as f:
        f.writelines(lines)
