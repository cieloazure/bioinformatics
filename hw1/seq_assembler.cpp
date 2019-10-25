#include <string>
#include <vector>
#include <algorithm>
#include <fstream>
#include <list>
#include <iostream>

namespace Assembler {
  class SequenceAssembler {
    std::string token = "[SEQUENCE ASSEMBLY]";

    bool starts_with(std::string s, std::string possible_prefix) {
      if(s.length() < possible_prefix.length()) return false;
        
      int idx = 0;
      // Ignore leading whitespace
      for(; idx < s.length() && std::isspace(s[idx]); idx++);

      // check possible prefix
      for(auto& c: possible_prefix) {
        // Differs
        if(idx >= s.length()) return false;
        if(s[idx] != c) return false;
        idx++;
      }
      return true;
    }

    void split_fragment(std::ofstream& outFile, std::string& fragment) {
      int i = 0;
      while(i < fragment.length()) {
        int j = i + 80  < fragment.length() + 1 ? i + 80 : fragment.length() + 1;
        outFile << fragment.substr(i, j - i) << std::endl;
        i += 80;
      }
    }

    public: 
      struct AlignmentResult {
        int score;
        std::string fragment;

        AlignmentResult() {}
        AlignmentResult(int s, std::string f): score{s}, fragment{f} {};
      };
      typedef AlignmentResult alignment_result;
      enum PtrVal {invalid, diag, up, left};

      void assemble(std::string input, int match_score, int replace_penalty, int del_ins_penalty, std::string output) {
        std::string err = token + ":Error";
        std::ofstream outFile(output, std::ios::out);
        if(!outFile) throw std::invalid_argument(err);
        std::ifstream inFile(input, std::ios::in);
        if(!inFile) throw std::invalid_argument(err);
        std::vector<std::string> fragments;
        std::string line;

        std::string token = "seq";
        int seq_num = 0;
        std::string curr_frag;
        while(std::getline(inFile, line)) {
          if(starts_with(line, ">")) {
            if(curr_frag.length() > 0) {
              // collect the frag
              fragments.push_back(curr_frag);
              // reset curr_frag
              curr_frag = "";
            }
          } else {
            curr_frag += line;
          }
        }


        if(curr_frag.length() > 0) {
          // collect the frag
          fragments.push_back(curr_frag);
          // reset curr_frag
          curr_frag = "";
        }

        // greedy_combine
        greedy_combine(fragments, match_score, replace_penalty, del_ins_penalty, seq_num, outFile);
      }

      void greedy_combine(std::vector<std::string>& fragments, int match_score, int replace_penalty, int del_ins_penalty, int seq_num, std::ofstream& outFile) {
        std::list<std::string> fragment_list;
        for(auto it = fragments.begin(); it != fragments.end(); it++) {
          fragment_list.push_front(*it);
        }

        while(fragment_list.size() > 1) {
          std::cout << fragment_list.size() << std::endl;
          alignment_result* max_result = nullptr;
          std::list<std::string>::iterator max_it;
          std::list<std::string>::iterator max_jt;

          //std::cout << "Fragment list size: " << fragment_list.size() << std::endl;

          // Align all fragments fi and fj with each other using dovetail alignment and compute
          // the alignment score vi,j
          for(auto it = fragment_list.begin(); it != fragment_list.end(); it++) {
            auto jt = it;
            std::advance(jt, 1);
            for(;jt != fragment_list.end(); jt++) {
              alignment_result* r = new alignment_result();
              alignment_score(*it, *jt, match_score, replace_penalty, del_ins_penalty, r);
              //std::cout << "(i, j):" << std::distance(fragment_list.begin(), it) << "," << std::distance(fragment_list.begin(), jt) << std::endl;
              //std::cout << "(score, fragment)" << r->score << "," <<r->fragment.length() << std::endl;
              if(max_result == nullptr || r->score > max_result->score) {
                max_result = r;
                max_it = it;
                max_jt = jt;
              }
            }
          }
          //Merge the two fragments fi and fj with the largest alignment score vi,j into a
          //new fragment f', if vi,j > 0
          
          //Replace fi and fj with the new fragment f' 
          //std::cout << "(max_i, max_j):" << std::distance(fragment_list.begin(), max_it) << "," << std::distance(fragment_list.begin(), max_jt) << std::endl;
          //std::cout << "(max_i_len, max_j_len)" << (*max_it).length() << "," << (*max_jt).length() << std::endl;
          //std::cout << "(score, fragment)" << max_result->score << "," << max_result->fragment.length() << std::endl;

          fragment_list.erase(max_it);
          fragment_list.erase(max_jt);
          fragment_list.push_front(max_result->fragment);
          // If largest alignment score is negative then break the loop
          if(max_result->score < 0) {
            break;
          }
        }

        // output combined seq to file
        outFile <<  "> Sequence_number_" << seq_num << "|" << "length " << fragment_list.front().length() << std::endl;
        split_fragment(outFile, fragment_list.front());
      }

     void alignment_score(std::string fragment1, std::string fragment2, int match_score, int replace_penalty, int del_ins_penalty, alignment_result* r) {
        int nrows = fragment1.length() + 1;
        int ncols = fragment2.length() + 1;
        std::vector<std::vector<int>> cost(nrows);
        std::vector<std::vector<PtrVal>> ptr(nrows);

        for(int i = 0; i <= fragment1.length(); i++) {
          cost[i] = std::vector<int>(ncols, 0);
        }

        for(int i = 0; i <= fragment1.length(); i++) {
          ptr[i] = std::vector<PtrVal>(ncols, static_cast<PtrVal>(0));
        }

        for(int i = 1; i <= fragment1.length(); i++) {
          for(int j = 1; j <= fragment2.length(); j++) {
            std::vector<int> possible_costs;
            if(fragment1[i-1] == fragment2[j-1]) {
              possible_costs.push_back(cost[i-1][j-1] + match_score);
            } else {
              possible_costs.push_back(cost[i-1][j-1] + replace_penalty);
            }
            possible_costs.push_back(cost[i-1][j] + del_ins_penalty);
            possible_costs.push_back(cost[i][j - 1] + del_ins_penalty);

            auto it = std::max_element(possible_costs.begin(), possible_costs.end());
            cost[i][j] = *it;
            ptr[i][j] = static_cast<PtrVal>(std::distance(possible_costs.begin(), it) + 1);
          }
        }

        //for(auto it = cost.begin(); it != cost.end(); it++) {
          //auto x = *it;
          //std::copy(x.begin(), x.end(), std::ostream_iterator<int>(std::cout, ","));
          //std::cout << std::endl;
        //}

        //std::cout << std::endl;
        //for(auto it = ptr.begin(); it != ptr.end(); it++) {
          //auto x = *it;
          //std::copy(x.begin(), x.end(), std::ostream_iterator<int>(std::cout, ","));
          //std::cout << std::endl;
        //}

        //std::cout << std::endl;
        auto it = std::max_element(cost.back().begin(), cost.back().end());

        int jt = -1;
        int jt_idx = -1;
        for(int i = 0; i < nrows; i++) {
          if(cost[i].back() > jt) {
            jt = cost[i].back();
            jt_idx = i;
          }
        }

        int max_score = -1;
        int match_idx = -1;
        std::string max_str;
        //std::cout << "(it, jt)" << "(" << *it << "," << jt << ")" << std::endl;
        if(*it > jt) {
          max_score = *it;
          match_idx = std::distance(cost.back().begin(), it);
          max_str = fragment1 + fragment2.substr(match_idx, fragment2.length() - match_idx);
        } else if(jt > *it) {
          max_score = jt;
          match_idx = jt_idx;
          max_str = fragment2 + fragment1.substr(match_idx, fragment1.length() - match_idx);
        } else {
          max_score = jt;
          int it_idx = std::distance(cost.back().begin(), it);
          //std::cout << "(it_idx, jt_idx)" << "(" << it_idx << "," << jt_idx << ")" << std::endl;
          if(jt_idx == ncols-1 || it_idx == nrows-1) {
            //std::cout << "Special condition 1" << std::endl;
            max_str = fragment1.length() > fragment2.length() ? fragment1 : fragment2;
          } else {
            //std::cout << "Special condition 2" << std::endl;
            int match_idx_1 = it_idx;
            int match_idx_2 = jt_idx;
            std::string max_str_1 = fragment1 + fragment2.substr(match_idx_1, fragment2.length() - match_idx_1);
            std::string max_str_2 = fragment2 + fragment1.substr(match_idx_2, fragment1.length() - match_idx_2);
            max_str = max_str_1.length() > max_str_2.length() ? max_str_1 : max_str_2;
          }
        }

        //std::cout << std::endl;
        //std::cout << match_idx << std::endl;

        //std::cout << std::endl;
        //std::cout << fragment2.substr(match_idx, fragment2.length() - match_idx) << std::endl;

        r->score = max_score;
        r->fragment = max_str;
      }

  };
};

int main(int argc, char *argv[]) {
  if(argc < 1 || argc > 6) throw std::invalid_argument("Invalid input");
  std::string input = argv[1];
  int match_score = std::stoi(argv[2]);
  int replace_pen = std::stoi(argv[3]);
  int del_ins_pen = std::stoi(argv[4]);
  std::string output = argv[5];

  Assembler::SequenceAssembler assm;
  //Assembler::SequenceAssembler::alignment_result r;
  //assm.alignment_score("aaaaabbb", "bbb", 2, -1, -1, &r);
  //std::cout << r.score << std::endl;
  //std::cout << r.fragment << std::endl;
  assm.assemble(input, match_score, replace_pen, del_ins_pen, output);
  //std::ofstream outFile("opt", std::ios::out);
  //std::vector<std::string> fragments;
  //fragments.push_back("actgtta");
  //fragments.push_back("gttactgt");
  //assm.greedy_combine(fragments, 2, -1, -1, 0, outFile);
  return 0;
}
